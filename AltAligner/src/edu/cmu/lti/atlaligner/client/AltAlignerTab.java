package edu.cmu.lti.atlaligner.client;

import com.google.gwt.user.client.ui.RootPanel;

public class AltAlignerTab {
	
	private SentenceAlignment sent;
	private int wordIndex ; // word to label
	private boolean indexOnSource ; // do we want to label target side or source side
	
	private RootPanel rootPanel;

	public SentenceAlignment getSent() {
		return sent;
	}

	public void setSent(SentenceAlignment sent) {
		this.sent = sent;
	}

	public int getWordIndex() {
		return wordIndex;
	}

	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}

	public boolean isIndexOnSource() {
		return indexOnSource;
	}

	public void setIndexOnSource(boolean indexOnSource) {
		this.indexOnSource = indexOnSource;
	}

	public RootPanel getRootPanel() {
		return rootPanel;
	}

	public void setRootPanel(RootPanel rootPanel) {
		this.rootPanel = rootPanel;
	} 
	
	public void render(){ // Render the information
		rootPanel.clear();
	}
	
	
}
