package app.embah.processing

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM5
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper

class ClientClassRemapper(cv: ClassVisitor?, remapper: Remapper): ClassRemapper(ASM5, cv, remapper) {

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        return ClientMethodVisitor(name, desc, super.visitMethod(access, name, desc, signature, exceptions))
    }

    inner class ClientMethodVisitor(val ownerName: String?, val ownerDesc: String?, val visitor: MethodVisitor): MethodVisitor(ASM5, visitor)  {

        override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String, itf: Boolean) {
            //can be used for detection purposes
            if (name == "getInputArguments") {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "app/embah/redirects/Redirects", name, desc, false)
            } else if (name.startsWith("getSerialNumber")) { //remap all HWID methods
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "app/embah/redirects/Redirects", "getSerialNumber", desc, false)
            } else if (owner == "java/lang/System" && name == "getProperty") {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "app/embah/redirects/Redirects", name, desc, false)
            } else super.visitMethodInsn(opcode, owner, name, desc, itf)
        }
    }
}