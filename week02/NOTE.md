学习笔记

# 一、GC日志分析 

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



## 2. 并行GC

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

2021-03-28T23:18:30.268-0800: 0.898: 
[Full GC (Ergonomics) 
	[PSYoungGen: 21485K->0K(153088K)]
	[ParOldGen: 313466K->238461K(349696K)] 334951K->238461K(502784K),
	[Metaspace: 3226K->3226K(1056768K)],
0.0519573 secs] 
[Times: user=0.08 sys=0.04, real=0.06 secs] 

2021-03-28T23:18:30.341-0800: 0.971: [GC (Allocation Failure) [PSYoungGen: 131584K->21500K(153088K)] 370045K->284439K(502784K), 0.0087418 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
2021-03-28T23:18:30.389-0800: 1.019: [GC (Allocation Failure) [PSYoungGen: 153084K->21486K(153088K)] 416023K->323582K(502784K), 0.0080753 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 

2021-03-28T23:18:30.397-0800: 1.027: [Full GC (Ergonomics)
[PSYoungGen: 21486K->0K(153088K)] 
[ParOldGen: 302096K->264025K(349696K)] 323582K->264025K(502784K),
[Metaspace: 3226K->3226K(1056768K)], 0.0743724 secs]
[Times: user=0.06 sys=0.00, real=0.08 secs] 

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

3次YoungGC, 每次GC耗时几十到一百多毫秒.

### 1.2 Xms512m Xms512m

```
2021-03-29T00:27:58.571-0800: [GC (Allocation Failure) 2021-03-29T00:27:58.571-0800: [ParNew: 139776K->17471K(157248K), 0.0579254 secs] 139776K->47628K(506816K), 0.0579949 secs] [Times: user=0.03 sys=0.02, real=0.05 secs] 
2021-03-29T00:27:58.734-0800: [GC (Allocation Failure) 2021-03-29T00:27:58.734-0800: [ParNew: 157247K->17469K(157248K), 0.0402426 secs] 187404K->91842K(506816K), 0.0402820 secs] [Times: user=0.03 sys=0.04, real=0.04 secs] 
2021-03-29T00:27:58.800-0800: [GC (Allocation Failure) 2021-03-29T00:27:58.800-0800: [ParNew: 157245K->17472K(157248K), 0.0551163 secs] 231618K->138482K(506816K), 0.0551546 secs] [Times: user=0.07 sys=0.01, real=0.05 secs] 
2021-03-29T00:27:58.879-0800: [GC (Allocation Failure) 2021-03-29T00:27:58.879-0800: [ParNew: 157248K->17472K(157248K), 0.0968402 secs] 278258K->185157K(506816K), 0.0968803 secs] [Times: user=0.13 sys=0.02, real=0.10 secs] 
2021-03-29T00:27:58.999-0800: [GC (Allocation Failure) 2021-03-29T00:27:58.999-0800: [ParNew: 156863K->17470K(157248K), 0.0843651 secs] 324548K->230073K(506816K), 0.0844132 secs] [Times: user=0.14 sys=0.02, real=0.09 secs] 
2021-03-29T00:27:59.084-0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 212603K(349568K)] 230774K(506816K), 0.0002456 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.084-0800: [CMS-concurrent-mark-start]
2021-03-29T00:27:59.092-0800: [CMS-concurrent-mark: 0.007/0.007 secs] [Times: user=0.01 sys=0.01, real=0.01 secs] 
2021-03-29T00:27:59.092-0800: [CMS-concurrent-preclean-start]
2021-03-29T00:27:59.093-0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.093-0800: [CMS-concurrent-abortable-preclean-start]
2021-03-29T00:27:59.151-0800: [GC (Allocation Failure) 2021-03-29T00:27:59.151-0800: [ParNew2021-03-29T00:27:59.198-0800: [CMS-concurrent-abortable-preclean: 0.001/0.105 secs] [Times: user=0.10 sys=0.03, real=0.10 secs] 
: 157246K->17470K(157248K), 0.0696555 secs] 369849K->272369K(506816K), 0.0697287 secs] [Times: user=0.10 sys=0.03, real=0.07 secs] 
2021-03-29T00:27:59.221-0800: [GC (CMS Final Remark) [YG occupancy: 17655 K (157248 K)]2021-03-29T00:27:59.221-0800: [Rescan (parallel) , 0.0014544 secs]2021-03-29T00:27:59.222-0800: [weak refs processing, 0.0000157 secs]2021-03-29T00:27:59.222-0800: [class unloading, 0.0041811 secs]2021-03-29T00:27:59.226-0800: [scrub symbol table, 0.0013388 secs]2021-03-29T00:27:59.228-0800: [scrub string table, 0.0001545 secs][1 CMS-remark: 254898K(349568K)] 272554K(506816K), 0.0072506 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.228-0800: [CMS-concurrent-sweep-start]
2021-03-29T00:27:59.229-0800: [CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.229-0800: [CMS-concurrent-reset-start]
2021-03-29T00:27:59.229-0800: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.270-0800: [GC (Allocation Failure) 2021-03-29T00:27:59.270-0800: [ParNew: 157246K->17470K(157248K), 0.0250851 secs] 369881K->274217K(506816K), 0.0251226 secs] [Times: user=0.04 sys=0.01, real=0.02 secs] 
2021-03-29T00:27:59.296-0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 256746K(349568K)] 274612K(506816K), 0.0001669 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.296-0800: [CMS-concurrent-mark-start]
2021-03-29T00:27:59.299-0800: [CMS-concurrent-mark: 0.003/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.299-0800: [CMS-concurrent-preclean-start]
2021-03-29T00:27:59.299-0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.299-0800: [CMS-concurrent-abortable-preclean-start]
2021-03-29T00:27:59.322-0800: [GC (Allocation Failure) 2021-03-29T00:27:59.322-0800: [ParNew: 157246K->17470K(157248K), 0.0325125 secs] 413993K->316529K(506816K), 0.0325525 secs] [Times: user=0.06 sys=0.01, real=0.03 secs] 
2021-03-29T00:27:59.378-0800: [GC (Allocation Failure) 2021-03-29T00:27:59.378-0800: [ParNew: 157246K->17470K(157248K), 0.0520529 secs] 456305K->361408K(506816K), 0.0520901 secs] [Times: user=0.05 sys=0.02, real=0.06 secs] 
2021-03-29T00:27:59.430-0800: [CMS-concurrent-abortable-preclean: 0.003/0.130 secs] [Times: user=0.15 sys=0.03, real=0.14 secs] 
2021-03-29T00:27:59.431-0800: [GC (CMS Final Remark) [YG occupancy: 17614 K (157248 K)]2021-03-29T00:27:59.431-0800: [Rescan (parallel) , 0.0024326 secs]2021-03-29T00:27:59.433-0800: [weak refs processing, 0.0000142 secs]2021-03-29T00:27:59.433-0800: [class unloading, 0.0002947 secs]2021-03-29T00:27:59.433-0800: [scrub symbol table, 0.0007132 secs]2021-03-29T00:27:59.434-0800: [scrub string table, 0.0001248 secs][1 CMS-remark: 343938K(349568K)] 361552K(506816K), 0.0036352 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.434-0800: [CMS-concurrent-sweep-start]
2021-03-29T00:27:59.436-0800: [CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2021-03-29T00:27:59.436-0800: [CMS-concurrent-reset-start]
2021-03-29T00:27:59.436-0800: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

YGC使用ParNew

| #    | Y before GC | Y after GC | Heap before GC | Heap after GC | 分析       |
| ---- | ----------- | ---------- | -------------- | ------------- | ---------- |
| 1    | 136         | 17         | 136            | 46            | 29m进了Old |
| 2    | 153         | 17         | 183            | 89            | 42m进了old |
| 3    | 153         | 17         | 226            | 135           | 45m进了old |
| 4    | 153         | 17         | 271            | 180           | 45m进了old |
| 5    | 153         | 17         | 317            | 224           | 43m进了old |

经过这几次YGC，大概有200m对象从young晋升到了old, 整个old大概分配了341m, 可能还有些大对象直接分配到了old。接下来可能产生了一个比较大的对象，old区没有足够大的连续内存进行分配，发生了CMS GC：

* 

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



## 5. 总结

### 5.1 Serial GC

* MinorGC: DefNew回收器，标记-复制算法，STW。
* MajorGC: Tenured回收器，标记-清除-整理算法，STW。作为CMS的后备预案，在并发收集发生Concurrent Mode Failure时使用。
* 因为GC是单线程的，比较适合单核机器，在多核机器上因为整个GC过程都是STW不能充分利用CPU资源，应用延迟会相对比较高。
* GC后存活对象越多，GC耗时会越高，所以内存配置的比较大的话GC耗时会很多，服务端一般不采用串行GC

### 5.2 Parallel GC

* MinorGC: Parallel Scavenge回收器, 多个线程并行收集，标记-复制算法，STW
* MajorGC: Parallel Old回收器，多个线程并行收集，标记-清除-整理算法，STW
* 多核机器下，相比Serial GC回收耗时要短很多
* CPU要么全部做GC，要么全部做业务，比较适合计算为主的后台应用

### 5.3 CMS GC

* MinorGC: ParNew回收器，多线程并行收集，标记-复制算法，STW
* MajorGC: CMS回收器，只收集老年代，标记-清除算法，分阶段执行，只有初始标记、最终标记阶段有STW，其它阶段和应用线程并发。分为以下7阶段
  * `初始标记`：STW, 标记GC Roots，GC Roots直接应用的对象，标记Old区被Young区存活对象直接引用的对象
  * `并发标记`：并发，根据`初始标记`的GC Roots遍历Old区所有存活对象，
  * `并发预清理`: 并发，使用卡片标记 `并发标记`引用发生变化的对象大致区域，即脏区，期间可能伴随多次MinorGC
  * `可取消的并发预清理`: 尽可能在`最终标记`前等待够长的时间，为`最终标记`多干些活，尽量降低STW的时间，当满足一定条件自动终止
  * `最终标记`：STW, 标记Old区所有存活对象，
  * `并发清理`: 并发，清理死亡的对象。
  * `并发重置`：并发，重置各种CMS参数，为下一次CMS循环准备。
* 因为CMS没有整理（使用空闲列表标记可用内存），因此会产生许多碎片，如果碎片太多以至于没有足够的空间放置从Young区晋升的对象或者直接分配的大对象，就会临时开启Serial Old清理老年代。
* 由于分阶段，并发，延迟相对Serial，Paralell低，比较适合交互较多的B/S服务端

### 5.4 G1 GC

* Heap划分成N个region, 每个region可能是Eden,S0,S1,old

* 每次GC都会收集young区，但是old区只会收集一部分。收集的对象集合称为collection set(日志里Cset)

* YoungGC ：Evacuation, 并行，STW, 标记-复制算法。最开始只有young区，当young区满了，存活对象被拷贝到存活区，如果没有存活区就任意选择一部分regions作为存活区

* 并发标记: 当堆内存使用比例达到 `initiatingHeapOccupancyPercent`触发，默认45%

  * `初始标记`: STW, 标记所有GCRoots直接可达对象
  * `根区扫描`: 并发，从初始标记的存活区扫描并标记对老年代的引用。并且只有完成该阶段后才能进行下一次STW年轻代垃圾回收
  * `并发标记`:并发，标记堆中所有存活对象。
  * `再次标记`:STW，标记所有并发标记阶段没有标记的存活对象
  * `清理`: 并发，STW都存在，统计region中存活的对象，并排序，为MixedGC打基础。此阶段，所有不包含存活对象的区域直接回收了

* MixedGC : STW, Evacuation : 回收young区+根据并发标记的结果优先收集垃圾比较多的old区。 在并发标记、MixedGC之间可能有多次ygc。

  