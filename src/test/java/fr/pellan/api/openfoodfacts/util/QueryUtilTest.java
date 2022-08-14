package fr.pellan.api.openfoodfacts.util;

@RunWith(SpringRunner.class )
@SpringBootTest
public class QueryUtilTest {

    @Autowired
    private QueryUtil queryUtil;

    public void testGetDataAsString(){
        assertNotNull(queryUtil.getDataAsString("https://www.google.com"));
    }
}
