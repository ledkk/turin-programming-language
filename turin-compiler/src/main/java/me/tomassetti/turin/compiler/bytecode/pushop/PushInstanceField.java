package me.tomassetti.turin.compiler.bytecode.pushop;

import me.tomassetti.turin.compiler.bytecode.BytecodeSequence;
import me.tomassetti.turin.jvm.JvmFieldDefinition;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETFIELD;

public class PushInstanceField extends BytecodeSequence {

    private JvmFieldDefinition fieldDefinition;
    private boolean pushThis;

    public PushInstanceField(JvmFieldDefinition fieldDefinition, boolean pushThis) {
        this.fieldDefinition = fieldDefinition;
        this.pushThis = pushThis;
    }

    public PushInstanceField(JvmFieldDefinition fieldDefinition) {
        this(fieldDefinition, true);
    }

    @Override
    public void operate(MethodVisitor mv) {
        if (fieldDefinition.isStatic()) {
            throw new UnsupportedOperationException();
        } else {
            if (pushThis) {
                PushThis.getInstance().operate(mv);
            }
            mv.visitFieldInsn(GETFIELD, fieldDefinition.getOwnerInternalName(), fieldDefinition.getFieldName(), fieldDefinition.getDescriptor());
        }
    }

}
