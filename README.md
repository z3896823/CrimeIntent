# CrimeIntent
Demo of Android Programming by The Big Nerd Ranch
first commit by zyb on 2017/03/17

本demo的设计令我大开眼界，有很多没见过的新奇设计，具体有什么神奇的效果还没感受出来，但好像耦合性降低了很多。
- 整个程序设计上，在寻找数据列表中的数据方面，一直使用对象本身所具有的id来寻找，而没有使用之前我用的根据数据在列表中的位置去寻找。感觉这样设计更加合理，因为位置是可能变化的，而ID是和对象绑定的，只要提供一个getId的方法就可以随时精确的找到相应的数据。

- 本demo认为从一个活动向另一个活动传输数据时不应该直接把数据扔过去，被启动的活动有义务告诉要启动它的活动它需要哪些数据，于是在非Main的Activity的出现了newIntent()方法。

- 本demo的设计者认为Activity就是用来托管Fragment的，所以当两个Activit中的代码几乎相同时可以将其抽象出一个抽象父类来，其中不相同的部分用抽象方法，让子类去实现。于是有了SingleFragmentActivity。

- 本demo还认为如果Fragment要从Activity中获得数据，显得二者绑定地太强，降低了Fragment的复用性，所以在Fragment中创建了一个newInstance()方法.该方法可以保证，Fragment需要的数据可以在Fragment被创建的时候，且在Attach给Activity之前，就获得它所需要的数据，这样就避免了在二者都生成之后才产生交互。
++这里需要注意到的是：经过newInstance()这种设计，Fragment实际上是跟特定的数据绑定了。++
newInstance()方法的作用相当于构造函数，为什么不直接使用构造函数呢？
这是整体设计要求导致的，因为某些托管Activity采用了抽象类的设计，此方法可以返回Fragment对象到抽象类的onCreate方法中去，构造方法也不是不可以写，但是增加了Activity与其抽象类的耦合性。

- 关于Fragment和Activity的关系，本书中的作者似乎是这样认为的：++虽然Activity和Fragment无法同时保持其相互独立型（也不需要），但是Activity应该知道Fragment的某些细节，而Fragment没必要知道Activity的细节，以此可以保证Fragment的独立性。++

- CrimeLab类的单例设计使得没有一个类真正拥有crimeList，任何类都可以随时获得crimeList，不像之前那样一个List是在活动之间传来传去。但是幸好这个demo不涉及到多线程，否则要考虑crimeList的线程安全性。

- 目前还不知道ViewPager是怎么加载Fragment的


2017.03.20项目总结：
1. 使用抽象类来建立专门用来托管Fragment的Activity
2. 使用newIntent来创建启动活动的Intent对象
3. 使用newInstance来创建要托管的Fragment对象，并使用setArgument给Fragment传递数据
4. 使用单例模式来管理模型
5. 使用setTargetFragment和onActivityResult在Fragment之间传递数据。


##使用回调可以在类与类之间顺畅沟通的情况下又很大程度上保证各自的独立性