package com.example.albert.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.BufferedReader;

import java.io.InputStreamReader;

public class RSAUtil {

    private android.content.Context context;

    public RSAUtil(android.content.Context context) {
        this.context = context;
    }

    public String RSAEncrypt(String preData) {
        final String demoJSpath = "demo.js";

        Context ct = Context.enter();
        ct.setOptimizationLevel(-1);
        Scriptable scope = ct.initStandardObjects();

        try {
            ct.evaluateReader(scope,
                    new BufferedReader(new InputStreamReader(this.context.getResources().getAssets().open(demoJSpath))),
                    null,
                    1, null);

            Function function = (Function) scope.get("encrptData", scope);
            Object result = function.call(ct, scope, scope, new String[] {preData});
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally{
            ct.exit();
        }

    }

}