# CrimeIntent
Demo of Android Programming by The Big Nerd Ranch
first commit by zyb on 2017/03/17

本demo的设计令我大开眼界，有很多没见过的新奇设计，具体有什么神奇的效果还没感受出来，但好像耦合性降低了很多。
- 本demo认为从一个活动向另一个活动传输数据时不应该直接把数据扔过去，被启动的活动有义务告诉要启动它的活动它需要哪些数据，于是在非Main的Activity的出现了newIntent()方法。

- 本demo的设计者认为Activity就是用来托管Fragment的，所以当两个Activit中的代码几乎相同时可以将其抽象出一个抽象父类来，其中不相同的部分用抽象方法，让子类去实现。于是有了SingleFragmentActivity。

- 本demo还认为如果Fragment要从Activity中获得数据，显得二者绑定地太强，降低了Fragment的复用性，所以在Fragment中创建了一个newInstance()方法.该方法可以保证，Fragment需要的数据可以在Fragment被创建的时候，且在Attach给Activity之前，就获得它所需要的数据，这样就避免了在二者都生成之后才产生交互。
++这里需要注意到的是：经过newInstance()这种设计，Fragment实际上是跟特定的数据绑定了。++

- 关于Fragment和Activity的关系，本书中的作者似乎是这样认为的：++虽然Activity和Fragment无法同时保持其相互独立型（也不需要），但是Activity应该知道Fragment的某些细节，而Fragment没必要知道Activity的细节，以此可以保证Fragment的独立性。++

- CrimeLab类的单例设计使得没有一个类真正持有crimeList，任何类都可以随时获得crimeList，不像之前那样一个List是在活动之间传来传去。但是幸好这个demo不涉及到多线程，否则要考虑crimeList的线程安全性。

妈的！给crime编号为什么要用什么鬼UUID！你他妈就用int不行吗！真是醉了！