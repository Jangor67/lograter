package nl.stokpop.lograter.gc.jmx.algorithm;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.stokpop.lograter.gc.jmx.MemoryMetrics;
import nl.stokpop.lograter.util.time.DateUtils;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemoryParallelGc implements MemoryMetrics {
    //Timestamp,HeapMemoryUsage,NonHeapMemoryUsage,PSMarkSweep,PSScavenge,Metaspace,PSOldGen,PSEdenSpace,CompressedClassSpace,CodeCache,PSSurvivorSpace
    @CsvBindByName
    private String timestamp;
    @CsvBindByName
    private long heapMemoryUsage;
    @CsvBindByName
    private long nonHeapMemoryUsage;
    @CsvBindByName(column = "pSMarkSweep")
    private long youngGenerationGcTime;
    @CsvBindByName(column = "pSScavenge")
    private long oldGenerationGcTime;
    @CsvBindByName
    private long metaspace;
    @CsvBindByName
    private long pSOldGen;
    @CsvBindByName
    private long pSEdenSpace;
    @CsvBindByName
    private long compressedClassSpace;
    @CsvBindByName
    private long codeCache;
    @CsvBindByName
    private long pSSurvivorSpace;

    @Override
    public long getTimestamp() {
        return DateUtils.parseISOTime(timestamp);
    }

    @Override
    public long getHeapMemoryUsedBytes() {
        return heapMemoryUsage;
    }

    @Override
    public long getEdenUsedBytes() {
        return pSEdenSpace;
    }

    @Override
    public long getSurvivorUsedBytes() {
        return pSSurvivorSpace;
    }

    @Override
    public long getTenuredUsedBytes() {
        return pSOldGen;
    }

    @Override
    public long getOldGenerationUsedBytes() {
        return pSOldGen;
    }

    @Override
    public long getMetaSpaceUsedBytes() {
        return metaspace;
    }

    @Override
    public long getCompressedClassSpaceUsedBytes() {
        return compressedClassSpace;
    }

    @Override
    public long getCodeCacheUsedBytes() {
        return codeCache;
    }

    @Override
    public double getGcDurationMs() {
        return getYoungGenerationGcTime() + getOldGenerationGcTime();
    }
}
