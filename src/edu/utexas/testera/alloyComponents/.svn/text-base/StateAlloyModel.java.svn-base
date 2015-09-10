package edu.utexas.testera.alloyComponents;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Attr;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.PrimSig;
import static edu.utexas.testera.utils.Constants.*;

public class StateAlloyModel extends AlloyModel {
	
	public StateAlloyModel() { 
		super();
		Signature stateSig = new Signature();
		stateSig.setLabel(stateSigName);		
		Signature preSig = new Signature();
		preSig.setLabel(preSigName);	
		Signature postSig = new Signature();
		postSig.setLabel(postSigName);	

		try {
			stateSig.setSig(new PrimSig(stateSigName, Attr.ABSTRACT));
			preSig.setSig(new PrimSig(preSigName, stateSig.getSig(), Attr.ONE));
			postSig.setSig(new PrimSig(postSigName, stateSig.getSig(), Attr.ONE));
		} catch (Err e) {
			// this error is not a stop breaker
			logger.error(e);
		}
		sigs.put(stateSigName, stateSig);
		
		// In this version the State only includes Pre, while testing for post-
		// conditions, the StateManager adds the Post sig to the model 
		// This is necessary to minimize the number of un-necessary instances 
		// generated during pre-state input generation
		// sigs.put(postSigName, postSig);
		sigs.put(preSigName, preSig);
		
		setModuleName("util/"+stateSigName);
	}
	
	public static String getPostSigDeclaration(){
		return "one sig " + postSigName + " extends " + stateSigName + " {}";
	}
}
