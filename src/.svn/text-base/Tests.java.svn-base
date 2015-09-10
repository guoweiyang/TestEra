import edu.utexas.testera.core.TestEra;
import edu.utexas.testera.tests.Node;
import edu.utexas.testera.tests.List;
import edu.utexas.testera.translator.StateManager;
import org.junit.Test;


public class Tests {
	@Test
	public void test10() {
		// TestEra Auto-Comment: Initialization statements
		List List_0 = new List();
		Node Node_0 = new Node();
		Node Node_1 = new Node();
		Node Node_2 = new Node();
		Node_0.next = Node_2;
		Node_1.next = Node_0;
		Node_2.next = Node_2;
		Node_0.i = 1;
		Node_1.i = 0;
		Node_2.i = 1;
		Node_0.a = false;
		Node_1.a = true;
		Node_2.a = false;
		// TestEra Auto-Comment: Pre-state abstraction
		StateManager sm = new StateManager();
		sm.addToState("List_0", List_0);
		sm.addToState("Node_0", Node_0);
		sm.addToState("Node_1", Node_1);
		sm.addToState("Node_2", Node_2);
		sm.generatePreState();
		// TestEra Auto-Comment: Invoke method under test
		List_0.removeInt(1);
		// TestEra Auto-Comment: Post-state checking
		TestEra.checkPostState(sm, "edu.utexas.testera.tests.List", "removeInt", "List_0", "1");
	}
}
