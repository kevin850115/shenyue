import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 15/3/10.
 */
public class TestFastJson {
    @Test
    public void test(){
        Map<ApplyAttr,String> map = new HashMap<ApplyAttr,String>();
        map.put(ApplyAttr.TEST1,"123");
        System.out.println(JSON.toJSONString(map));
    }


}

enum ApplyAttr{
    TEST1("test"),
    TEST2("test2");

    private String key;

    ApplyAttr(String key) {
        this.key = key;
    }
}
