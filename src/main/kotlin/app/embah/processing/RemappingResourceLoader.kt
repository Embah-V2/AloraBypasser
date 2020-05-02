package app.embah.processing

import app.embah.commons.utils.Configuration
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.tree.ClassNode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class RemappingResourceLoader(directory: String) {
 
    val client: ResourceArchive

    init {
        val file = File(directory + "client.jar")
        //make a copy of the raw client into the resources folder
        file.copyTo(File("src/main/resources/client.jar"), overwrite = true)
        client = ResourceArchive(bytesFromFile(file))
    }

    companion object {

        const val REDIRECT_PATH = "app/embah/redirects/Redirects"

        fun bytesFromFile(file: File): ByteArray = Files.readAllBytes(file.toPath())
    }

    inner class ResourceArchive(bytes: ByteArray) {
        val resourceMap = HashMap<String, ByteArray>()
        val classMap    = HashMap<String, ClassNode>()
        val remapper    = object: Remapper() {
            override fun map(typeName: String?): String {
                //remap java/net/NetworkInterface to our Redirect class
                return if (typeName == "java/net/NetworkInterface") {
                    REDIRECT_PATH
                            .also { if (Configuration.debugging) println("getHardwareAddress() successfully remapped") }
                } else super.map(typeName)
            }
        }

        init {
            val zipInputStream   = ZipInputStream(bytes.inputStream() as InputStream?)
            var entry: ZipEntry? = zipInputStream.nextEntry

            while (entry != null) {

                val outStream = ByteArrayOutputStream().also { out ->
                    zipInputStream.copyTo(out)
                }
                val entryBytes = outStream.toByteArray()

                if (entry.name.endsWith(".class")) {
                    val reader = ClassReader(entryBytes)
                    val node = ClassNode()
                    reader.accept(ClientClassRemapper(node, remapper), ClassReader.EXPAND_FRAMES)
                    classMap[entry.name.replace(".class", "").replace("/", ".")] = node
                } else {
                    resourceMap[entry.name] = entryBytes
                }
                entry = zipInputStream.nextEntry
            }
        }

        fun outputAsJar(path: String) {
            val jar = JarOutputStream(FileOutputStream(path))
            classMap.forEach {
                jar.putNextEntry(JarEntry(it.key.replace(".", "/").plus(".class")))
                val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
                it.value.accept(cw)
                jar.write(cw.toByteArray())
                jar.closeEntry()
            }
            resourceMap.forEach {
                jar.putNextEntry(JarEntry(it.key))
                jar.write(it.value)
                jar.closeEntry()
            }
            jar.close()
        } 
    }
}
