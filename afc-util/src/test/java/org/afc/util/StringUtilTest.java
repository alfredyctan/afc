package org.afc.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;


public class StringUtilTest {

	@Rule
	public TestName name = new TestName();

	@Before
	public void setUp() throws Exception {
		JUnit4Util.startCurrentTest(getClass(), name);
	}
	
	@After
	public void tearDown() throws Exception {
		JUnit4Util.endCurrentTest(getClass(), name);
	}

	@Test
	public void testNamedMatch() throws Exception {
		Map<String, String> actual = JUnitUtil.actual(StringUtil.namedMatch("(?<type>^.*?) (?<key>.*?) (?<comment>.*$)", "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQCb2avNb4LN1ZhFlVj5G0O/ErKliKP4ics3s2mNxRBLFcjoZjbCe5SNCScQOJbQcAjQ79RxNRVxUPwcWeja/zAVi0tYUGlk1b57C98nq4sO8QDHkDWTAlWwdVdi52XlAQhs/6Q0NYHkOQ6YADVLXYXLjii86gNVLhVq23fTep4MQrwa2jtEaaN0y7BezqAiKxO6mGvlhDoDge4T2onk2ZO5Z9fQF2hBoxRcWDSGOYp/sHdmvPMKrBQaGwWKW8rcSgqHj7rJW+8RlvdT3GguAdp7Q0HaRf36XY4EgVO4C6V4SIPxpYpQyeTm9HTnvz0W3+UNn3297P4Zck8g70ub0TtDsWUHmSygdlzqCpMSsKhjEq99dS/WZ2Ef9dJXdO0FO0d1kI58LWrArO4uEvfDiB72sICWOMMD6U2+1hnsZ9yhHJ9VrFALbCCZJhwTcqbKJwGqWYpUlY2B5rSxa+hdeBNFmxAITpeOXuuVWNnmxgDqrkYiuXzWpoMYzzB9Ng6SJeY9c8kiiJOE1ITMIj5xxBNGv6lYsxzg7RZ9cZ1sk0cSlZxuI2sSn9rgPRXmxHS/xKl2MFeefQ8KbSRjU5fBFSKbTZvudCl9IVFaLdTBHd3GEme8scXPaFJF5M/Wz1d2+jzeTsUH1m62lkk6p0slpuCsX2atq9OF1hj+HtDJcJ0IqQ==   ubuntu.dev@afc.com.hk"));
		Map<String, String> expect = JUnitUtil.expect(MapUtil.map(new HashMap<String, String>(),
			"type", "ssh-rsa",
			"key", "AAAAB3NzaC1yc2EAAAADAQABAAACAQCb2avNb4LN1ZhFlVj5G0O/ErKliKP4ics3s2mNxRBLFcjoZjbCe5SNCScQOJbQcAjQ79RxNRVxUPwcWeja/zAVi0tYUGlk1b57C98nq4sO8QDHkDWTAlWwdVdi52XlAQhs/6Q0NYHkOQ6YADVLXYXLjii86gNVLhVq23fTep4MQrwa2jtEaaN0y7BezqAiKxO6mGvlhDoDge4T2onk2ZO5Z9fQF2hBoxRcWDSGOYp/sHdmvPMKrBQaGwWKW8rcSgqHj7rJW+8RlvdT3GguAdp7Q0HaRf36XY4EgVO4C6V4SIPxpYpQyeTm9HTnvz0W3+UNn3297P4Zck8g70ub0TtDsWUHmSygdlzqCpMSsKhjEq99dS/WZ2Ef9dJXdO0FO0d1kI58LWrArO4uEvfDiB72sICWOMMD6U2+1hnsZ9yhHJ9VrFALbCCZJhwTcqbKJwGqWYpUlY2B5rSxa+hdeBNFmxAITpeOXuuVWNnmxgDqrkYiuXzWpoMYzzB9Ng6SJeY9c8kiiJOE1ITMIj5xxBNGv6lYsxzg7RZ9cZ1sk0cSlZxuI2sSn9rgPRXmxHS/xKl2MFeefQ8KbSRjU5fBFSKbTZvudCl9IVFaLdTBHd3GEme8scXPaFJF5M/Wz1d2+jzeTsUH1m62lkk6p0slpuCsX2atq9OF1hj+HtDJcJ0IqQ==",
			"comment", "  ubuntu.dev@afc.com.hk" 
		));
		assertThat("match", actual.entrySet(), everyItem(isIn(expect.entrySet())));
	}
	
	@Test
	public void testRightCut() throws Exception {
		String actual = JUnitUtil.actual(StringUtil.rightCut("ubuntu.dev@afc.com.hk", 3));
		String expect = JUnitUtil.expect("ubuntu.dev@afc.com");
		assertThat("match", actual, is(equalTo(expect)));
	}

	@Test
	public void testLeftCut() throws Exception {
		String actual = JUnitUtil.actual(StringUtil.leftCut("ubuntu.dev@afc.com.hk", 10));
		String expect = JUnitUtil.expect("@afc.com.hk");
		assertThat("match", actual, is(equalTo(expect)));
	}

}
