package com.zoctan.api.util;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DnamicCompilerHelp {

    public String GetCompeleteClass(String SourceClassScript, Long Caseid) {
        String javaSrc = "import com.zoctan.api.util.MD5;" +
                "import com.zoctan.api.util.AES;" +
                "import com.zoctan.api.util.Base64;" +
                "import com.zoctan.api.util.DES;" +
                "import com.zoctan.api.util.AutoMeter;" +
                "public class TestCasePreClass{" +
                "public String AutoMeterPreScriptFun() {" +
                "AutoMeter.caseid=new Long(" + Caseid + ");" +
                SourceClassScript +
                "}" +
                "}";
        return javaSrc;
    }

    public Object CallDynamicScript(String javaSrc) throws Exception {
        Map<String, byte[]> results = new HashMap<>();
        StringWriter sw=new StringWriter();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager);
        JavaFileObject javaFileObject = manager.makeStringSource("TestCasePreClass.java", javaSrc);
        JavaCompiler.CompilationTask task = compiler.getTask(sw, manager, null, null, null, Arrays.asList(javaFileObject));
        if (task.call()) {
            results = manager.getClassBytes();
        }
        else
        {
            throw new Exception("脚本编译出错，请检查语法错误："+sw);
        }
        MemoryClassLoader classLoader = new MemoryClassLoader(results);
        Class clazz = classLoader.loadClass("TestCasePreClass");
        Object object = clazz.newInstance();
        Method method = clazz.getMethod("AutoMeterPreScriptFun");
        Object Result= method.invoke(object);
        return Result;
    }
}
