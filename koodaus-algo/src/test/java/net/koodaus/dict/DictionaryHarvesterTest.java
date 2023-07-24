package net.koodaus.dict;

import net.koodaus.algo.FpxCrc32;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mzinal
 */
public class DictionaryHarvesterTest {

    @Test
    public void testHarvester() {
        Dictionary namesFirstMale = MemoryDictionary.load("../dict-data/test/names-first-male.txt");
        Assert.assertEquals("names-first-male", namesFirstMale.getName());
        Dictionary namesFirstFemale = MemoryDictionary.load("../dict-data/test/names-first-female.txt");
        Assert.assertEquals("names-first-female", namesFirstFemale.getName());
        Dictionary namesLastMale = MemoryDictionary.load("../dict-data/test/names-last-male.txt");
        Assert.assertEquals("names-last-male", namesLastMale.getName());
        Dictionary namesLastFemale = MemoryDictionary.load("../dict-data/test/names-last-female.txt");
        Assert.assertEquals("names-last-female", namesLastFemale.getName());
        Dictionary namesMiddleMale = namesFirstMale.extractExtra("names-middle-male", 0);
        Assert.assertEquals(namesFirstMale.size(), namesMiddleMale.size());
        Dictionary namesMiddleFemale = namesFirstMale.extractExtra("names-middle-female", 1);
        Assert.assertEquals(namesFirstMale.size(), namesMiddleFemale.size());
        
        FpxCrc32 indexerFactory = new FpxCrc32("здравствуйте, я ваша тётя!");
        DictionaryHarvester harvestMale = new DictionaryHarvester(indexerFactory,
                namesLastMale, namesFirstMale, namesMiddleMale);
        DictionaryHarvester harvestFemale = new DictionaryHarvester(indexerFactory,
                namesLastFemale, namesFirstFemale, namesMiddleFemale);

        String male1 = harvestMale.harvest(7000000L);
        Assert.assertEquals("сизый аристоклий панкратиевич", male1.toLowerCase());
        String male2 = harvestMale.harvest(7000001L);
        Assert.assertEquals("ивлев клавдий аттиевич", male2.toLowerCase());

        String female1 = harvestFemale.harvest(7000000L);
        Assert.assertEquals("сизая севастьяна панкратиевна", female1.toLowerCase());
        String female2 = harvestFemale.harvest(7000001L);
        Assert.assertEquals("ивлева северина аттиевна", female2.toLowerCase());
    }

}
