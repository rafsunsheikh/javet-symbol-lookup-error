package com.example;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.exceptions.JavetException;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting the Node.js runtime...");

        try (JavetEnginePool<NodeRuntime> enginePool = new JavetEnginePool<>()) {
            enginePool.getConfig().setJSRuntimeType(JSRuntimeType.Node);
            try (IJavetEngine<NodeRuntime> engine = enginePool.getEngine()) {
                NodeRuntime nodeRuntime = engine.getV8Runtime();
                nodeRuntime.allowEval(true);
                nodeRuntime.getExecutor("console.log('Node.js version:', process.version);").executeVoid();
                nodeRuntime.getExecutor("console.log('V8 version:', process.versions.v8);").executeVoid();

                // Load the bufferutil module
                nodeRuntime.getExecutor("const bufferutil = require('bufferutil');")
                        .setModule(true)
                        .executeVoid();

                System.out.println("Successfully loaded bufferutil.");
            }
        } catch (JavetException e) {
            e.printStackTrace();
        }
    }
}
