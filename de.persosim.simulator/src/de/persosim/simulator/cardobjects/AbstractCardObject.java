package de.persosim.simulator.cardobjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.persosim.simulator.exception.AccessDeniedException;
import de.persosim.simulator.exception.LifeCycleChangeException;
import de.persosim.simulator.exception.ObjectNotModifiedException;
import de.persosim.simulator.secstatus.SecStatus;

/**
 * Abstract superclass for most/all CardObjects. This implements handling of
 * bidirectional parent/child relation.
 * 
 * @author amay
 * 
 */
public abstract class AbstractCardObject implements CardObject {

	protected CardObject parent;
	protected List<CardObject> children = new ArrayList<>();
	SecStatus securityStatus;
	
	protected Iso7816LifeCycleState lifeCycleState = Iso7816LifeCycleState.CREATION;

	@Override
	public void setSecStatus(SecStatus securityStatus) throws AccessDeniedException{
		if (!lifeCycleState.isPersonalizationPhase()){
			throw new AccessDeniedException("The security status can not be set after leaving the personalization phase");
		}
		this.securityStatus = securityStatus;
		
		//forward the SecStatus to all children
		for (CardObject curChild : getChildren()) {
			curChild.setSecStatus(securityStatus);
		}
	}
	
	@Override
	public CardObject getParent() {
		return parent;
	}

	@Override
	public Collection<CardObject> getChildren() {
		return children;
	}

	/**
	 * Add new child to the collection.
	 * <p/>
	 * This method also sets the SecStatus of the new child. If the new child is of
	 * type AbstractCardObject also the parent is set.
	 * 
	 * @param newChild
	 *            child to add to the collection
	 * @throws AccessDeniedException 
	 */
	public void addChild(CardObject newChild) {
		children.add(newChild);
		if (newChild instanceof AbstractCardObject) {
			((AbstractCardObject) newChild).parent = this;
		}
		try {
			newChild.setSecStatus(securityStatus);
		} catch (AccessDeniedException e) {
			throw new ObjectNotModifiedException("A new child should have security access restriction that allows setting the security status");
		}
	}

	/**
	 * Remove child from the collection.
	 * 
	 * If the given child is of type AbstractCardObject its parent field will be
	 * reset to null after successful removal.
	 * 
	 * If the given element is not a child nothing will be done at all.
	 * 
	 * @param child
	 *            element to remove from the collection
	 */
	public void removeChild(CardObject child) {
		if (children.contains(child)) {
			children.remove(child);
			if (child instanceof AbstractCardObject) {
				((AbstractCardObject) child).parent = null;
			}
		}
	}

	@Override
	public Iso7816LifeCycleState getLifeCycleState() {
		return lifeCycleState;
	}

	@Override
	public void updateLifeCycleState(Iso7816LifeCycleState state) throws LifeCycleChangeException {		
		if (lifeCycleState.isPersonalizationPhase() && 
				state.equals(Iso7816LifeCycleState.OPERATIONAL_ACTIVATED)){
			lifeCycleState = state;
			return;
		}
		
		switch (getLifeCycleState()){
		case INITIALISATION:
			if(state.equals(Iso7816LifeCycleState.OPERATIONAL_ACTIVATED)){
				lifeCycleState = state;
			}
			break;
		case CREATION:
			if(state.equals(Iso7816LifeCycleState.INITIALISATION) || state.equals(Iso7816LifeCycleState.OPERATIONAL_ACTIVATED)){
				lifeCycleState = state;
			}
			break;
		case OPERATIONAL_ACTIVATED:
			if(state.equals(Iso7816LifeCycleState.OPERATIONAL_DEACTIVATED) || state.equals(Iso7816LifeCycleState.TERMINATION)){
				lifeCycleState = state;
			}
			break;
		case OPERATIONAL_DEACTIVATED:
			if(state.equals(Iso7816LifeCycleState.OPERATIONAL_ACTIVATED) || state.equals(Iso7816LifeCycleState.TERMINATION)){
				lifeCycleState = state;
			}
			break;
		default:
			throw new LifeCycleChangeException("Change is not allowed.", lifeCycleState, state);
		}
		
	}
	
	@Override
	public Collection<CardObject> findChildren(CardObjectIdentifier... cardObjectIdentifiers) {
		if(cardObjectIdentifiers.length == 0) {throw new IllegalArgumentException("must provide at least 1 identifier");}
		
		Collection<CardObject> matchingChildren = new ArrayList<>();
		
		//check the immediate children of the current DF
		boolean fullMatch;
		for (CardObject curChild : getChildren()){
			fullMatch = true;
			for(CardObjectIdentifier cardObjectIdentifier : cardObjectIdentifiers) {
				if (!cardObjectIdentifier.matches(curChild)){
					fullMatch = false;
					break;		
				}
			}
			
			if(fullMatch) {
				matchingChildren.add(curChild);
			}
		}
		
		// if no fitting child has been found, collection is empty
		return matchingChildren;
	}
	
}
