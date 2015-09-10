package edu.utexas.testera.tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4.Pos;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;

public class RunAlsFile {
	public static void main(String[] args) throws Exception {

		// The visualizer (We will initialize it to nonnull when we visualize an Alloy solution)
        VizGUI viz = null;

        // Alloy4 sends diagnostic messages and progress reports to the A4Reporter.
        // By default, the A4Reporter ignores all these events (but you can extend the A4Reporter to display the event for the user)
        A4Reporter rep = new A4Reporter() {
            // For example, here we choose to display each "warning" by printing it to System.out
            @Override public void warning(ErrorWarning msg) {
                System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
                System.out.flush();
            }
        };
        String filename = "testEra/test.als";
		 
		{
            // Parse+typecheck the model
            System.out.println("=========== Parsing+Typechecking "+filename+" =============");
            CompModule world = CompUtil.parseEverything_fromFile(rep, null, filename);
            
            //Expr newFact = world.parseOneExpressionFromString("all n: Node | n in List.head.*next");
            //world.addFact(world.getAllFacts().get(0).b.pos(), "postfact", newFact);
            System.out.println(world.getAllFacts());
            // Choose some default options for how you want to execute the commands
            A4Options options = new A4Options();
            options.solver = A4Options.SatSolver.SAT4J;

            for (Command command: world.getAllCommands()) {
                // Execute the command
                System.out.println("============ Command "+command+": ============");
                //Command newCommand = command.change(command.formula.and(newFact));
                A4Solution ans = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), command, options);
                /*
                // Print the outcome
                System.out.println(ans);
                // If satisfiable...
                if (ans.satisfiable()) {
                    // You can query "ans" to find out the values of each set or type.
                    // This can be useful for debugging.
                    //
                    // You can also write the outcome to an XML file
                    ans.writeXML("alloy_example_output.xml");
                    //
                    // You can then visualize the XML file by calling this:
                    if (viz==null) {
                        viz = new VizGUI(false, "alloy_example_output.xml", null);
                    } else {
                        viz.loadXML("alloy_example_output.xml", true);
                    }
                }*/
                
                
                A4Solution temp = ans;
                System.exit(1);
                int q = 0;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                long t1 = System.currentTimeMillis(), t2=t1, t3=t1;
                int minStmts = 100;
                while (temp!=null)
                {
                	System.out.println(temp);
                	/*
                	if (temp.satisfiable()) {
                        // You can query "ans" to find out the values of each set or type.
                        // This can be useful for debugging.
                        //
                        // You can also write the outcome to an XML file
                        temp.writeXML("alloy_example_output.xml");
                        //
                        // You can then visualize the XML file by calling this:
                        if (viz==null) {
                            viz = new VizGUI(false, "alloy_example_output.xml", null);
                        } else {
                            viz.loadXML("alloy_example_output.xml", true);
                        }
                    }*/
                	System.out.println("A4Solution "+q++ +" found in "+(t3 - t2) +" ms");
                	t2 = System.currentTimeMillis();
                	A4Solution temp2 = temp.next();
                	t3 = System.currentTimeMillis();
                	if (temp2 != temp)
                		temp = temp2;
                	else
                		temp = null;
                }
                System.out.println("Total time: " + (t3-t1)); 
            }
        }
	}
}
