学习笔记

# 一、GC日志分析 （作业01,04）

* GCLogAnalysis.java 创建随机对象，用于观察GC情况

```java
package javademo.w02.gcanalysis;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
/*
演示GC日志生成与解读
*/
public class GCLogAnalysis {
    private static Random random = new Random();
    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数; 可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象; 进入老年代
        int cacheSize = 2000;
        Object[] cachedGarbage = new Object[cacheSize];
        // 在此时间范围内,持续循环
        while (System.currentTimeMillis() < endMillis) {
            // 生成垃圾对象
            Object garbage = generateGarbage(100*1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cachedGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束!共生成对象次数:" + counter.longValue());
    }

    // 生成对象
    private static Object generateGarbage(int max) {
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result = null;
        switch (type) {
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize) {
                    builder.append(randomString);
                    builder.append(max);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
```

* 环境: Mac 4核 8G, jdk8

## 1. 串行GC

### 1.1 Xms1g Xmx1g

* 启动参数: -Xmx1g -Xms1g -XX:-UseAdaptiveSizePolicy -XX:+UseSerialGC 

* 输出: 

```
2021-03-28T22:33:47.137-0800: 0.516: [GC (Allocation Failure) 2021-03-28T22:33:47.137-0800: 0.516: [DefNew: 279616K->34944K(314560K), 0.2502715 secs] 279616K->83771K(1013632K), 0.2508129 secs] [Times: user=0.03 sys=0.03, real=0.25 secs] 
2021-03-28T22:33:47.482-0800: 0.860: [GC (Allocation Failure) 2021-03-28T22:33:47.482-0800: 0.860: [DefNew: 314560K->34942K(314560K), 0.1132800 secs] 363387K->158545K(1013632K), 0.1133684 secs] [Times: user=0.04 sys=0.03, real=0.11 secs] 
2021-03-28T22:33:47.644-0800: 1.023: [GC (Allocation Failure) 2021-03-28T22:33:47.644-0800: 1.023: [DefNew: 314558K->34943K(314560K), 0.1007363 secs] 438161K->236190K(1013632K), 0.1008723 secs] [Times: user=0.03 sys=0.03, real=0.10 secs] 
Heap
 def new generation   total 314560K, used 272226K [0x0000000780000000, 0x0000000795550000, 0x0000000795550000)
  eden space 279616K,  84% used [0x0000000780000000, 0x000000078e7b8c60, 0x0000000791110000)
  from space 34944K,  99% used [0x0000000793330000, 0x000000079554fd08, 0x0000000795550000)
  to   space 34944K,   0% used [0x0000000791110000, 0x0000000791110000, 0x0000000793330000)
 tenured generation   total 699072K, used 201247K [0x0000000795550000, 0x00000007c0000000, 0x00000007c0000000)
   the space 699072K,  28% used [0x0000000795550000, 0x00000007a19d7e68, 0x00000007a19d8000, 0x00000007c0000000)
 Metaspace       used 3234K, capacity 4500K, committed 4864K, reserved 1056768K
  class space    used 358K, capacity 388K, committed 512K, reserved 1048576K
```

| #    | 类型     | Young before GC/mb | Young after GC/mb | Heap before GC/mb | Heap after GC/mb | 耗时/ms | 分析                                             |
| ---- | -------- | ------------------ | ----------------- | ----------------- | ---------------- | ------- | ------------------------------------------------ |
| 1    | Young GC | 273                | 34                | 273               | 82               | 250     | Young  回收了239m, Heap整体回收了191约48m进入old |
| 2    | Young GC | 307                | 34                | 354               | 154              | 113     | Young  回收了273m, Heap整体回收了200约73m进入old |
| 3    | Young GC | 307                | 34                | 428               | 231              | 100     | Young  回收了273m, Heap整体回收了197约76m进入old |

只发生了Young GC, 3次回收共约197m进入old，这与` tenured generation   total 699072K, used 201247K ` 是吻合的。另外GC的耗时是比较长的，因为是单线程进行回收，效率不高。

### 1.2 Xms512m Xmx512m

* 启动参数:-Xms512m -Xmx512m  -XX:-UseAdaptiveSizePolicy -XX:+UseSerialGC 
* 输出

```
2021-03-28T23:08:57.687-0800: 0.364: [GC (Allocation Failure) 2021-03-28T23:08:57.687-0800: 0.364: [DefNew: 139761K->17471K(157248K), 0.0738055 secs] 139761K->38545K(506816K), 0.0741989 secs] [Times: user=0.02 sys=0.01, real=0.08 secs] 
2021-03-28T23:08:57.810-0800: 0.487: [GC (Allocation Failure) 2021-03-28T23:08:57.810-0800: 0.487: [DefNew: 157212K->17471K(157248K), 0.0560652 secs] 178285K->91722K(506816K), 0.0561790 secs] [Times: user=0.02 sys=0.03, real=0.05 secs] 
2021-03-28T23:08:57.898-0800: 0.575: [GC (Allocation Failure) 2021-03-28T23:08:57.898-0800: 0.575: [DefNew: 156974K->17471K(157248K), 0.0517558 secs] 231225K->133150K(506816K), 0.0518551 secs] [Times: user=0.02 sys=0.01, real=0.06 secs] 
2021-03-28T23:08:58.070-0800: 0.747: [GC (Allocation Failure) 2021-03-28T23:08:58.070-0800: 0.747: [DefNew: 157112K->17468K(157248K), 0.0858056 secs] 272791K->182906K(506816K), 0.0859121 secs] [Times: user=0.02 sys=0.01, real=0.08 secs] 
2021-03-28T23:08:58.180-0800: 0.857: [GC (Allocation Failure) 2021-03-28T23:08:58.181-0800: 0.857: [DefNew: 157200K->17467K(157248K), 0.0304995 secs] 322638K->220864K(506816K), 0.0306000 secs] [Times: user=0.01 sys=0.02, real=0.03 secs] 
2021-03-28T23:08:58.233-0800: 0.910: [GC (Allocation Failure) 2021-03-28T23:08:58.233-0800: 0.910: [DefNew: 156719K->17471K(157248K), 0.0321153 secs] 360116K->262596K(506816K), 0.0322041 secs] [Times: user=0.02 sys=0.01, real=0.03 secs] 
2021-03-28T23:08:58.290-0800: 0.966: [GC (Allocation Failure) 2021-03-28T23:08:58.290-0800: 0.967: [DefNew: 157247K->17471K(157248K), 0.0470791 secs] 402372K->303874K(506816K), 0.0471749 secs] [Times: user=0.02 sys=0.01, real=0.04 secs] 
2021-03-28T23:08:58.386-0800: 1.063: [GC (Allocation Failure) 2021-03-28T23:08:58.388-0800: 1.065: [DefNew: 157247K->17472K(157248K), 0.0687538 secs] 443650K->351418K(506816K), 0.0707407 secs] [Times: user=0.02 sys=0.02, real=0.07 secs] 
2021-03-28T23:08:58.483-0800: 1.160: [GC (Allocation Failure) 2021-03-28T23:08:58.483-0800: 1.160: [DefNew: 157248K->157248K(157248K), 0.0000382 secs]2021-03-28T23:08:58.483-0800: 1.160: [Tenured: 333946K->274244K(349568K), 0.0715932 secs] 491194K->274244K(506816K), [Metaspace: 3228K->3228K(1056768K)], 0.0717593 secs] [Times: user=0.06 sys=0.01, real=0.07 secs] 
Heap
 def new generation   total 157248K, used 8494K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,   6% used [0x00000007a0000000, 0x00000007a084ba40, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
  to   space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
 tenured generation   total 349568K, used 274244K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 349568K,  78% used [0x00000007aaaa0000, 0x00000007bb6711a0, 0x00000007bb671200, 0x00000007c0000000)
 Metaspace       used 3326K, capacity 4500K, committed 4864K, reserved 1056768K
  class space    used 366K, capacity 388K, committed 512K, reserved 1048576K
```

和1g堆内存相比， GC更频繁，每次回收的垃圾数量减少，耗时减少，最终old区对象变多了。

## 2. 并行GC

### 1.1 Xms1g Xmx1g

* 启动参数:-Xms1g -Xmx1g  -XX:-UseAdaptiveSizePolicy -XX:+UseParallelGC
* 输出

```
2021-03-28T23:21:12.890-0800: 0.502: [GC (Allocation Failure) [PSYoungGen: 262144K->43518K(305664K)] 262144K->79525K(1005056K), 0.1111086 secs] [Times: user=0.04 sys=0.04, real=0.11 secs] 
2021-03-28T23:21:13.119-0800: 0.731: [GC (Allocation Failure) [PSYoungGen: 305662K->43502K(305664K)] 341669K->154518K(1005056K), 0.1763091 secs] [Times: user=0.06 sys=0.07, real=0.18 secs] 
2021-03-28T23:21:13.558-0800: 1.170: [GC (Allocation Failure) [PSYoungGen: 305646K->43519K(305664K)] 416662K->226598K(1005056K), 0.1606583 secs] [Times: user=0.04 sys=0.04, real=0.16 secs] 
Heap
 PSYoungGen      total 305664K, used 51998K [0x00000007aab00000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 262144K, 3% used [0x00000007aab00000,0x00000007ab347b08,0x00000007bab00000)
  from space 43520K, 99% used [0x00000007bab00000,0x00000007bd57fe70,0x00000007bd580000)
  to   space 43520K, 0% used [0x00000007bd580000,0x00000007bd580000,0x00000007c0000000)
 ParOldGen       total 699392K, used 183079K [0x0000000780000000, 0x00000007aab00000, 0x00000007aab00000)
  object space 699392K, 26% used [0x0000000780000000,0x000000078b2c9cf0,0x00000007aab00000)
 Metaspace       used 3737K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 418K, capacity 428K, committed 512K, reserved 1048576K
```

3次YGC, 耗时110-120ms。

### 1.2 Xms512m Xmx512m

* 启动参数:-Xms512m -Xmx512m  -XX:-UseAdaptiveSizePolicy -XX:+UseParallelGC
* 输出

```
2021-03-28T23:18:29.652-0800: 0.282: [GC (Allocation Failure) [PSYoungGen: 131584K->21494K(153088K)] 131584K->38441K(502784K), 0.0158300 secs] [Times: user=0.01 sys=0.02, real=0.01 secs] 
2021-03-28T23:18:29.726-0800: 0.357: [GC (Allocation Failure) [PSYoungGen: 153078K->21488K(153088K)] 170025K->79754K(502784K), 0.0539156 secs] [Times: user=0.03 sys=0.04, real=0.06 secs] 
2021-03-28T23:18:29.821-0800: 0.452: [GC (Allocation Failure) [PSYoungGen: 153072K->21484K(153088K)] 211338K->115642K(502784K), 0.0613028 secs] [Times: user=0.02 sys=0.02, real=0.06 secs] 
2021-03-28T23:18:29.933-0800: 0.563: [GC (Allocation Failure) [PSYoungGen: 153068K->21497K(153088K)] 247226K->156326K(502784K), 0.0654948 secs] [Times: user=0.03 sys=0.03, real=0.07 secs] 
2021-03-28T23:18:30.032-0800: 0.662: [GC (Allocation Failure) [PSYoungGen: 153081K->21490K(153088K)] 287910K->207798K(502784K), 0.0438413 secs] [Times: user=0.03 sys=0.02, real=0.04 secs] 
2021-03-28T23:18:30.113-0800: 0.743: [GC (Allocation Failure) [PSYoungGen: 153074K->21495K(153088K)] 339382K->256520K(502784K), 0.0367596 secs] [Times: user=0.02 sys=0.02, real=0.04 secs] 
2021-03-28T23:18:30.177-0800: 0.807: [GC (Allocation Failure) [PSYoungGen: 153079K->21501K(153088K)] 388104K->291644K(502784K), 0.0371174 secs] [Times: user=0.02 sys=0.03, real=0.04 secs] 
2021-03-28T23:18:30.236-0800: 0.866: [GC (Allocation Failure) [PSYoungGen: 153085K->21485K(153088K)] 423228K->334951K(502784K), 0.0318950 secs] [Times: user=0.03 sys=0.02, real=0.03 secs] 
2021-03-28T23:18:30.268-0800: 0.898: [Full GC (Ergonomics) [PSYoungGen: 21485K->0K(153088K)] [ParOldGen: 313466K->238461K(349696K)] 334951K->238461K(502784K), [Metaspace: 3226K->3226K(1056768K)], 0.0519573 secs] [Times: user=0.08 sys=0.04, real=0.06 secs] 
2021-03-28T23:18:30.341-0800: 0.971: [GC (Allocation Failure) [PSYoungGen: 131584K->21500K(153088K)] 370045K->284439K(502784K), 0.0087418 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
2021-03-28T23:18:30.389-0800: 1.019: [GC (Allocation Failure) [PSYoungGen: 153084K->21486K(153088K)] 416023K->323582K(502784K), 0.0080753 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
2021-03-28T23:18:30.397-0800: 1.027: [Full GC (Ergonomics) [PSYoungGen: 21486K->0K(153088K)] [ParOldGen: 302096K->264025K(349696K)] 323582K->264025K(502784K), [Metaspace: 3226K->3226K(1056768K)], 0.0743724 secs] [Times: user=0.06 sys=0.00, real=0.08 secs] 
2021-03-28T23:18:30.513-0800: 1.144: [GC (Allocation Failure) [PSYoungGen: 131510K->21485K(153088K)] 395536K->310308K(502784K), 0.0197316 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 
Heap
 PSYoungGen      total 153088K, used 48033K [0x00000007b5580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 131584K, 20% used [0x00000007b5580000,0x00000007b6f6d148,0x00000007bd600000)
  from space 21504K, 99% used [0x00000007bd600000,0x00000007beafb5a0,0x00000007beb00000)
  to   space 21504K, 0% used [0x00000007beb00000,0x00000007beb00000,0x00000007c0000000)
 ParOldGen       total 349696K, used 288822K [0x00000007a0000000, 0x00000007b5580000, 0x00000007b5580000)
  object space 349696K, 82% used [0x00000007a0000000,0x00000007b1a0dae8,0x00000007b5580000)
 Metaspace       used 3634K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 409K, capacity 428K, committed 512K, reserved 1048576K
```

GC更频繁，耗时更短，而且产生了2次FullGC。FullGC耗时整体比YGC要长一点。

## 3. CMS

### 1.1 Xms1g Xmx1g

```
2021-03-28T23:31:12.173-0800: 0.668: [GC (Allocation Failure) 2021-03-28T23:31:12.174-0800: 0.668: [ParNew: 272640K->34048K(306688K), 0.1387337 secs] 272640K->78513K(1014528K), 0.1404050 secs] [Times: user=0.05 sys=0.05, real=0.14 secs] 
2021-03-28T23:31:12.472-0800: 0.967: [GC (Allocation Failure) 2021-03-28T23:31:12.472-0800: 0.967: [ParNew: 306688K->34048K(306688K), 0.0844260 secs] 351153K->162297K(1014528K), 0.0845175 secs] [Times: user=0.06 sys=0.05, real=0.08 secs] 
2021-03-28T23:31:12.610-0800: 1.105: [GC (Allocation Failure) 2021-03-28T23:31:12.610-0800: 1.105: [ParNew: 306688K->34048K(306688K), 0.0646376 secs] 434937K->236056K(1014528K), 0.0647273 secs] [Times: user=0.10 sys=0.03, real=0.06 secs] 
Heap
 par new generation   total 306688K, used 179915K [0x0000000780000000, 0x0000000794cc0000, 0x0000000794cc0000)
  eden space 272640K,  53% used [0x0000000780000000, 0x0000000788e72f18, 0x0000000790a40000)
  from space 34048K, 100% used [0x0000000792b80000, 0x0000000794cc0000, 0x0000000794cc0000)
  to   space 34048K,   0% used [0x0000000790a40000, 0x0000000790a40000, 0x0000000792b80000)
 concurrent mark-sweep generation total 707840K, used 202008K [0x0000000794cc0000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 3619K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 407K, capacity 428K, committed 512K, reserved 1048576K
```



### 1.2 Xms512m Xms512m

```
2021-03-28T23:32:33.387-0800: 0.492: [GC (Allocation Failure) 2021-03-28T23:32:33.387-0800: 0.492: [ParNew: 139776K->17472K(157248K), 0.0213096 secs] 139776K->47428K(506816K), 0.0214605 secs] [Times: user=0.03 sys=0.03, real=0.02 secs] 
2021-03-28T23:32:33.462-0800: 0.567: [GC (Allocation Failure) 2021-03-28T23:32:33.462-0800: 0.567: [ParNew: 157248K->17472K(157248K), 0.0627437 secs] 187204K->97907K(506816K), 0.0628478 secs] [Times: user=0.03 sys=0.03, real=0.06 secs] 
2021-03-28T23:32:33.562-0800: 0.667: [GC (Allocation Failure) 2021-03-28T23:32:33.562-0800: 0.667: [ParNew: 157204K->17472K(157248K), 0.0975909 secs] 237640K->140966K(506816K), 0.0976939 secs] [Times: user=0.06 sys=0.02, real=0.10 secs] 
2021-03-28T23:32:33.695-0800: 0.799: [GC (Allocation Failure) 2021-03-28T23:32:33.695-0800: 0.799: [ParNew: 157248K->17472K(157248K), 0.0841198 secs] 280742K->184509K(506816K), 0.0842261 secs] [Times: user=0.12 sys=0.02, real=0.08 secs] 
2021-03-28T23:32:33.809-0800: 0.914: [GC (Allocation Failure) 2021-03-28T23:32:33.809-0800: 0.914: [ParNew: 156684K->17472K(157248K), 0.0622219 secs] 323721K->228240K(506816K), 0.0623285 secs] [Times: user=0.07 sys=0.02, real=0.07 secs] 
2021-03-28T23:32:33.872-0800: 0.977: [GC (CMS Initial Mark) [1 CMS-initial-mark: 210768K(349568K)] 235130K(506816K), 0.0019531 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-28T23:32:33.874-0800: 0.979: [CMS-concurrent-mark-start]
2021-03-28T23:32:33.882-0800: 0.986: [CMS-concurrent-mark: 0.008/0.008 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
2021-03-28T23:32:33.882-0800: 0.987: [CMS-concurrent-preclean-start]
2021-03-28T23:32:33.884-0800: 0.988: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-28T23:32:33.884-0800: 0.988: [CMS-concurrent-abortable-preclean-start]
2021-03-28T23:32:33.899-0800: 1.003: [GC (Allocation Failure) 2021-03-28T23:32:33.899-0800: 1.003: [ParNew: 157119K->17472K(157248K), 0.0696202 secs] 367887K->276796K(506816K), 0.0697288 secs] [Times: user=0.07 sys=0.02, real=0.07 secs] 
2021-03-28T23:32:33.993-0800: 1.097: [GC (Allocation Failure) 2021-03-28T23:32:33.993-0800: 1.097: [ParNew: 157248K->17471K(157248K), 0.0471707 secs] 416572K->321323K(506816K), 0.0472767 secs] [Times: user=0.04 sys=0.01, real=0.05 secs] 
2021-03-28T23:32:34.071-0800: 1.176: [GC (Allocation Failure) 2021-03-28T23:32:34.072-0800: 1.176: [ParNew: 157247K->157247K(157248K), 0.0000366 secs]2021-03-28T23:32:34.072-0800: 1.176: [CMS2021-03-28T23:32:34.072-0800: 1.176: [CMS-concurrent-abortable-preclean: 0.005/0.188 secs] [Times: user=0.23 sys=0.04, real=0.19 secs] 
 (concurrent mode failure): 303851K->258096K(349568K), 0.0681035 secs] 461099K->258096K(506816K), [Metaspace: 3730K->3730K(1056768K)], 0.0682799 secs] [Times: user=0.06 sys=0.00, real=0.07 secs] 
Heap
 par new generation   total 157248K, used 73239K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,  52% used [0x00000007a0000000, 0x00000007a4785d20, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
  to   space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
 concurrent mark-sweep generation total 349568K, used 258096K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 3737K, capacity 4540K, committed 4864K, reserved 1056768K
  class space    used 418K, capacity 428K, committed 512K, reserved 1048576K
```



## 4. G1

### 1.1 Xms1g Xmx1g

```
2021-03-28T23:34:33.264-0800: 0.344: [GC pause (G1 Evacuation Pause) (young) 63M->22M(1024M), 0.0092068 secs]
2021-03-28T23:34:33.298-0800: 0.378: [GC pause (G1 Evacuation Pause) (young) 74M->38M(1024M), 0.0151376 secs]
2021-03-28T23:34:33.338-0800: 0.418: [GC pause (G1 Evacuation Pause) (young) 90M->58M(1024M), 0.0100201 secs]
2021-03-28T23:34:33.368-0800: 0.448: [GC pause (G1 Evacuation Pause) (young) 114M->76M(1024M), 0.0059054 secs]
2021-03-28T23:34:33.430-0800: 0.510: [GC pause (G1 Evacuation Pause) (young) 168M->104M(1024M), 0.0208118 secs]
2021-03-28T23:34:33.491-0800: 0.571: [GC pause (G1 Evacuation Pause) (young) 194M->129M(1024M), 0.0125900 secs]
2021-03-28T23:34:33.546-0800: 0.625: [GC pause (G1 Evacuation Pause) (young) 247M->166M(1024M), 0.0240479 secs]
2021-03-28T23:34:33.604-0800: 0.684: [GC pause (G1 Evacuation Pause) (young) 284M->199M(1024M), 0.0219336 secs]
2021-03-28T23:34:33.698-0800: 0.778: [GC pause (G1 Evacuation Pause) (young) 359M->244M(1024M), 0.0468439 secs]
2021-03-28T23:34:33.781-0800: 0.861: [GC pause (G1 Evacuation Pause) (young) 406M->296M(1024M), 0.0442632 secs]
2021-03-28T23:34:33.879-0800: 0.959: [GC pause (G1 Evacuation Pause) (young) 489M->343M(1024M), 0.1084599 secs]
2021-03-28T23:34:34.014-0800: 1.094: [GC pause (G1 Evacuation Pause) (young) 443M->366M(1024M), 0.0270215 secs]
2021-03-28T23:34:34.116-0800: 1.196: [GC pause (G1 Evacuation Pause) (young) 549M->416M(1024M), 0.0751828 secs]
```



### 1.2 Xms512m Xmx512m

```
2021-03-28T23:35:36.483-0800: 0.290: [GC pause (G1 Evacuation Pause) (young) 33M->11M(512M), 0.0252671 secs]
2021-03-28T23:35:36.522-0800: 0.329: [GC pause (G1 Evacuation Pause) (young) 41M->20M(512M), 0.0138451 secs]
2021-03-28T23:35:36.547-0800: 0.355: [GC pause (G1 Evacuation Pause) (young) 51M->27M(512M), 0.0034667 secs]
2021-03-28T23:35:36.665-0800: 0.473: [GC pause (G1 Evacuation Pause) (young) 89M->52M(512M), 0.0633272 secs]
2021-03-28T23:35:36.742-0800: 0.549: [GC pause (G1 Evacuation Pause) (young) 96M->65M(512M), 0.0369630 secs]
2021-03-28T23:35:36.808-0800: 0.615: [GC pause (G1 Evacuation Pause) (young) 132M->86M(512M), 0.0232755 secs]
2021-03-28T23:35:36.872-0800: 0.679: [GC pause (G1 Evacuation Pause) (young) 166M->117M(512M), 0.0410610 secs]
2021-03-28T23:35:36.983-0800: 0.790: [GC pause (G1 Evacuation Pause) (young) 215M->146M(512M), 0.0384371 secs]
2021-03-28T23:35:37.260-0800: 1.068: [GC pause (G1 Humongous Allocation) (young) (initial-mark)-- 401M->226M(512M), 0.0411295 secs]
2021-03-28T23:35:37.302-0800: 1.109: [GC concurrent-root-region-scan-start]
2021-03-28T23:35:37.302-0800: 1.109: [GC concurrent-root-region-scan-end, 0.0002226 secs]
2021-03-28T23:35:37.302-0800: 1.109: [GC concurrent-mark-start]
2021-03-28T23:35:37.305-0800: 1.112: [GC concurrent-mark-end, 0.0031241 secs]
2021-03-28T23:35:37.305-0800: 1.112: [GC remark, 0.0013658 secs]
2021-03-28T23:35:37.307-0800: 1.114: [GC cleanup 244M->244M(512M), 0.0006452 secs]
2021-03-28T23:35:37.325-0800: 1.133: [GC pause (G1 Evacuation Pause) (young) 303M->248M(512M), 0.0079309 secs]
2021-03-28T23:35:37.336-0800: 1.144: [GC pause (G1 Evacuation Pause) (mixed) 264M->223M(512M), 0.0041365 secs]
2021-03-28T23:35:37.342-0800: 1.149: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 230M->224M(512M), 0.0009648 secs]
2021-03-28T23:35:37.343-0800: 1.150: [GC concurrent-root-region-scan-start]
2021-03-28T23:35:37.343-0800: 1.151: [GC concurrent-root-region-scan-end, 0.0001477 secs]
2021-03-28T23:35:37.343-0800: 1.151: [GC concurrent-mark-start]
2021-03-28T23:35:37.345-0800: 1.152: [GC concurrent-mark-end, 0.0017085 secs]
2021-03-28T23:35:37.345-0800: 1.152: [GC remark, 0.0011432 secs]
2021-03-28T23:35:37.346-0800: 1.154: [GC cleanup 234M->233M(512M), 0.0003598 secs]
2021-03-28T23:35:37.347-0800: 1.154: [GC concurrent-cleanup-start]
2021-03-28T23:35:37.347-0800: 1.154: [GC concurrent-cleanup-end, 0.0000105 secs]
2021-03-28T23:35:37.394-0800: 1.201: [GC pause (G1 Evacuation Pause) (young)-- 414M->303M(512M), 0.0077951 secs]
2021-03-28T23:35:37.403-0800: 1.211: [GC pause (G1 Evacuation Pause) (mixed) 311M->293M(512M), 0.0083563 secs]
```

