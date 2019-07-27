package a4;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

class HeapTest {

	@Test
	void testPoll() {
		Heap<String, Integer> h = new Heap<String, Integer>(Comparator.naturalOrder());
//		h.poll();
		h.add("a", 1);
		h.poll();
	}

}
