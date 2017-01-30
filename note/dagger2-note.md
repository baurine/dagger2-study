# Dagger2 Note

## References

1. [Android 常用开源工具 (1) - Dagger2 入门](http://blog.csdn.net/duo2005duo/article/details/50618171)
1. [Android 常用开源工具 (2) - Dagger2 进阶](http://blog.csdn.net/duo2005duo/article/details/50696166)
1. [Dagger2 使用详解](http://zpayh.xyz/2016/07/07/Dagger2%E4%BD%BF%E7%94%A8%E8%AF%A6%E8%A7%A3/)
1. [dagger2Demo](https://github.com/luxiaoming/dagger2Demo)
1. [使用 dagger2 来做依赖注入，以及在单元测试中的应用](http://chriszou.com/2016/05/10/android-unit-testing-di-dagger.html)

## Note 1

Note for [Android 常用开源工具 (1) - Dagger2 入门](http://blog.csdn.net/duo2005duo/article/details/50618171)

2017/1/30

> 上面例子面临着一个问题，Container 依赖了 Apple 实现，如果某一天需要修改 Apple 为 Banana，那么你一定得改 Container 的代码。有没有一种方法可以不改 Container 呢?

将动态部分抽离，抽离成可配置 (可配置 --> XML? 这就是原来的依赖注入为什么用 XML 的原因?) 这同时也让我再一次想起了之前对 [linux 驱动的理解](http://baurine.github.io/2013/06/02/linux_subsystem_device_mode.html)。一样的思想。灵活灵活再灵活。

> 我的理解是，在没有设备模型的情况下，设备的一些硬件参数，比如寄存器地址，中断号，都是直接写在驱动中的，耦合性比较大，一旦这些参数变了，整个驱动就要重编。
> 设备模型首先把这部分内容抽取出来，放到了一个独立的结构体中，比如 `platform_device`，用来描述一个设备，我觉得它相当于一个配置文件。
> 驱动通过 `platform_get_resource()` 从 `platform_device` 中获取寄存器地址或中断号。如果参数改变，则只需修改 `platform_device` 即可，驱动本身无需任何修改。

2016/8/1

Module / Component / Container

就像 ButterKnife 需要手动调用 `ButterKnife.bind(this)` 实现注入一样，Dagger2 一样需要 (我想应该是所有的注入框架都需要的)，使用 `Component.create().inject(this)` 来手动注入一下。

Component 是中间的协调者，~~它负责生成 Module，然后把 Module 注入到 Container 中。~~ 它负责从对应的 Module 中获取 Container 依赖的对象，把它们注入到 Container 中。注意，Module 是依赖的对象的生产工厂，它负责产生依赖对象。

典型的代码，模块：

- Model: Fruit / Apple / Banana / FruitInfo
- Module: FruitModule
- Component: FruitComponent
- Container: FruitContainer

`Component.create().inject(container)`

这句语句把  Module 和 Container 通过 Component 关联在一起。
Component 在执行 `inject()` 时，在 Container 中寻找用 `@Inject` 注解标记的变量，得到它们的类型，即 class，比如上例中的 Fruit。然后 Component 到它关联的所有 modules 里面查找返回值为 Fruit 类型的用 `@Provides` 注解的方法 (方法名任意，只关心返回值类型)，然后调用此方法生成目标对象，赋值给 Container 中的变量。

如果 Module 中的产生对象的方法需要某种类型的参数，则首先在 Module 中查找有没有返回值为此类型的用 `@Provides` 注释的方法，如果没有，则继续查找此类型的定义中有没有用 `@Inject` 注解的构造函数。

如果某个类型具有用 `@Inject` 注释的构造函数时，那么这个类型的获取就不一定需要通过 Module 来获得了，可以直接在 Componet 中定义，如下所示：

    // 定义 ComponentB
    // 假设 Module 中没有 provideApple() 方法,但有 provideInfo()
    @Component(modules={xxx})
    interface ComponentB {
        // 实现类自动返回由 Apple(info) 构建的实现类
        // getApple() 这个方法名可以任意，重点是返回值类型
        Apple getApple(); 
    }

    public class Apple {
        //被@Inject标记，使用这个构造器生成实例
        @Inject
        Apple(Info info){
            ...
        }

        //不会使用这个构造器，没有被@Inject标记
        Apple() { 
        }
    }

当有多个返回相同类型的 `@Provides` 方法时，用 `@Named("xx")` 来区分。

从这几个概念来看，Dagger2 并不复杂。

Component 的依赖：...

Container 的 `@Inject` 规则：

1. 标注的变量包级可见，不能是 private 变量
1. 标记的变量，查找对应依赖的规则：
   - 该成员变量的依赖会从 Module 的 `@Provides` 方法集合中查找；
   - 如果查找不到，则查找成员变量类型是否有 `@Inject` 构造方法，并注入构造方法且递归注入该类型的成员变量

## Note 2

Note for [Android 常用开源工具 (2) - Dagger2 进阶](http://blog.csdn.net/duo2005duo/article/details/50696166)

1. 实现单例，`@Singleton`

   需要同时标记在 Module 和 Component 上，且这个单例只存在同一个 Component 里，并不是一个全局静态变量，不同的 Component 里的这个变量值是不一样的。

        @Module
        class MachineModule{
            // 添加 @Singleton 标明该方法产生只产生一个实例
            @Singleton 
            @Provides
            Machine provideFruitJuiceMachine(){
                return new FruitJuiceMachine();
            }
        }

        // 添加 @Singleton 标明该 Component 中有 Module 使用了 @Singleton
        @Singleton 
        @Component(modules=MachineModule.class)
        class JuiceComponent{
            void inject(Container container)
        }

1. Scope

1. SubComponent

   通过 Subcomponent，子 Component 就好像同时拥有两种 Scope，当注入的元素来自父 Component 的 Module，则这些元素会缓存在父 Component，当注入的元素来自子 Component 的 Module，则这些元素会缓存在子 Component 中。

1. Lazy 与 Provider，明白了。一图胜千言，十码胜千言。

        public class Container{
            // 注入 Lazy 元素
            @Inject Lazy<Fruit> lazyFruit; 
            // 注入 Provider 元素
            @Inject Provider<Fruit> providerFruit; 
            public void init(){
                DaggerComponent.create().inject(this);
                // 在这时才创建 f1，以后每次调用 get 会得到同一个 f1 对象
                Fruit f1=lazyFruit.get(); 
                // 在这时创建 f2，以后每次调用 get 会再强制调用 Module 的 Provides 方法一次，
                // 根据 Provides 方法具体实现的不同，可能返回跟 f2 是同一个对象，也可能不是。
                Fruit f2=providerFruit.get(); 
            }
        }

## Note 3

Note for [Dagger2 使用详解](http://zpayh.xyz/2016/07/07/Dagger2%E4%BD%BF%E7%94%A8%E8%AF%A6%E8%A7%A3/)

这篇文章解答了我的一些疑问。即 Module 确实不是必须的，但 Component 是必须的。
Component 可以直接调用所需要注入的类的构造函数来生成所需的注入对象，而不经过 Module。前提是构造函数使用 `@Inject` 注解标注。
但是，如果要注入的对象并不是自己写的，而是第三方写的，那么这时候就需要 Module 来包装了。
而且，Container 也一般不需要额外再定义，Activity 一般就是作为 Container。

> 有时候我们并不能直接在构造函数里面添加 `@Inject` 注解，或者类中存在多个构造函数时，`@Inject` 也只能注解其中一个构造函数，不能注解多个构造函数，这里是会产生歧义性，因为 Dagger2 无法确认调用哪一个构造函数来生成例的实例对象。另外一种情况是我们在项目中引用第三方类库时，也是无法直接在类构造函数中添加 `@Inject` 注解的，所以我们需要用到 `@Module` 注解了。

`@Module` 注解的类是一个工厂类，集中创建要注入的类的对象实例。

这篇文章的条理也讲得不错。感觉比上一篇也写得更好。但是缺少一点图，要是加上图就更完美了。

理解以下概念即可：

- `@Inject`，`@Module`，`@Provides`，`@Component`
- `@Scope`：别把 Scope 想得太复杂，其实 `@Singleton` 就是 `@Scope` 的一个默认实现而已。`@Scope` 就是用来声明注入时不要重复生成新实例，只要生成一次实例。

Dagger2 框架提供的是 `@Scope` 和 `@Qualifier` 注解，`@Singleton` 和 `@Named` 为 Dagger2 为了方便我们使用，分别用它们包装了一下 `@Scope` 和 `@Qualifier` 而已。
你真正应该理解的是 `@Scope` 和 `@Qualifier` 的原理，而不是 `@Singleton` 和 `@Named` 的原理。

`Component.create().inject(container)`，这里的 container 对象不能是基类，必须是最终的具体的子类。比如要注入到两个不同的 Activity 中，不能只用一个 `Component.create().inject(Activity activity)` 来简化，必须分别定义，类似如下：

    public abstract void inject(MainActivity activity);
    public abstract void inject(SecondActivity activity);

因为 Dagger 在寻找需要注入的变量时，只会根据类型直接定位到对应的类中，不是根据继承关系去寻找子类。

组织 Component (还是没太明白)，有两种方式：

1. 一种是依赖方式，在子 component 的 `@Component` 参数里声明依赖的 Component，如下所示：

        // 这里表示 Component 会从 MainModule 类中拿那些用 @Provides 注解的方法来生成需要注入的实例
        @PoetryScope
        @Component(dependencies = ApplicationComponent.class, modules = {MainModule.class,PoetryModule.class})
        public abstract class MainComponent {
          ...
        }

1. 一种是用 `@Subcomponent` 注解在子 Component 中声明，如下所示：

        @AScope
        @Subcomponent(modules = AModule.class)
        public interface AComponent {
            void inject(AActivity activity);
        }

重新再看一遍，结合代码。彻底明白了。

1. 用依赖的方式，子 Component 只能得到在父 Component 中声明的类型，但是用 `@SubComponent` 的方式，子 Component 可以得到父 Component 中所有的注入类型。
1. 用不同方式，生成 Component 的方式也不同。

依赖方式：(MainComponent 依赖于 ApplicationComponent)

    public static MainComponent getInstance() {
        if (instance == null) {
            instance = DaggerMainComponent.builder()
                    .applicationComponent(MyApplication.getInstance().applicationComponent())
                    .build();
        }
        return instance;
    }

SubComponent 方式：(AComponent 是一个 SubComponent，在 ApplicationComponent 中定义 `AComponent getACompoenet()` 的方法）

    MyApplication.getInstance().applicationComponent().getAComponent().inject(this);

## Note 4

Note for [dagger2Demo](https://github.com/luxiaoming/dagger2Demo)

Dagger2 在 android 项目的常见用法。

貌似是经常定义一个 Application 级别的 Module，在这里提供一些全局变量，比如 context，local manager 之类的。

记住， `@Singleton` 是作用在 Component 级别，使同一个 Component 里只产生一次某种类型的变量，对于 Component 本身来说，并不能使 Component 成为单例。

虽然 Dagger 2 的原理和用法是掌握了，但如何在 android 项目中应用还是不太清楚。
