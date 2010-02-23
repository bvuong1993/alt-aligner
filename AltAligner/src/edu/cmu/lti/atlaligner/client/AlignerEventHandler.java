package edu.cmu.lti.atlaligner.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;

public class AlignerEventHandler implements ClickHandler, MouseOverHandler, MouseOutHandler{
	
	private AtlAligner app = null;
	public void setApp(AtlAligner a){
		app =a;
	}

	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource() instanceof Button){
			Button bt = (Button) event.getSource();
			if(bt.isVisible() && bt.isEnabled()){
				if (bt.getStylePrimaryName().indexOf("Sentence")>0){
					app.buttonClicked(bt);
				}
			}
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		if(event.getSource() instanceof Button){
			Button bt = (Button) event.getSource();
			if(bt.isVisible() && bt.isEnabled()){
				if (bt.getStylePrimaryName().indexOf("Sentence")>0){
					//bt.removeStyleDependentName("mousehover");
					bt.addStyleDependentName("mousehover");
				}
			}
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if(event.getSource() instanceof Button){
			Button bt = (Button) event.getSource();
			if(bt.isVisible() && bt.isEnabled()){
				if (bt.getStylePrimaryName().indexOf("Sentence")>0){
					bt.removeStyleDependentName("mousehover");
					//bt.addStyleDependentName("mousehover");
				}
			}
		}
	}

}
