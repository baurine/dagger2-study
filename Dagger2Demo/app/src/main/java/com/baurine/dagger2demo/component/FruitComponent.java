package com.baurine.dagger2demo.component;

import com.baurine.dagger2demo.container.FruitContainer;
import com.baurine.dagger2demo.module.FruitModule;

import dagger.Component;

/**
 * Created by baurine on 1/30/17.
 */

// 3. @Component 指明这个是 Component 类，
// modules 参数指明去哪些 module 里查找依赖
@Component(modules = {FruitModule.class})
// 4. Component 是一个接口类，实现类由 dagger2 自动生成，
// 对应生成的类名为 DaggerXxx，此例中生成的类为 DaggerFruitComponent
public interface FruitComponent {
    // 5. 注入方法，在 Container 中被调用
    void inject(FruitContainer fruitContainer);
}
