package edu.cmu.lti.atlaligner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

import edu.cmu.lti.atlaligner.client.SentenceAlignment.AlignmentLink;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AtlAligner implements EntryPoint {

	private static String content="This is the content";
	
	// The main panel
	private VerticalPanel mainPanel = new VerticalPanel();
	
	// Before the target sentence panel, we start from an information panel, which is also vertical
	private VerticalPanel infoPanel = new VerticalPanel();
	// The information panel contains a title label, a description label, and a tab-like horizontal panel 
	// that contains all the words ppl want to label
	private Label titleLabel = new Label("Label the translation of highlighted words");
	private Button titlehelp = new Button("Click to toggle help");
	private String openHelp = "Click here to show help";
	private String closeHelp = "Click here to hide help";
	private Button titledemo = new Button("Click to toggle demo");
	private String openDemo = "Click here to show demo";
	private String closeDemo = "Click here to hide demo";
	
	private Label titleLabel2 = new Label("Words to label:");
	private VerticalPanel descPanel = new VerticalPanel();
	private VerticalPanel demoPanel = new VerticalPanel();
	private HTML descHTML = new HTML();
	private HTML demoHTML = new HTML();
	private HorizontalPanel taskPanel = new HorizontalPanel();
	private ArrayList<Button> taskButtons = new ArrayList<Button>(); // All the buttons, each for a task
	private HTML statusLabel = new HTML();
	
	// Top part, include the target sentence in the form of scrolling panel
	private HorizontalPanel tgtPanel = new HorizontalPanel();
	private ScrollPanel upperPanel = new ScrollPanel();
	private HorizontalPanel upperSentPanel = new HorizontalPanel();
	private ArrayList<Button> targetWords = new ArrayList<Button>();
	
	// Upper-Middle part, space 
	private GWTCanvas middleSpace = new GWTCanvas(1024,200);

	// Middle part, Source sentence

	private HorizontalPanel srcPanel = new HorizontalPanel();
	private ScrollPanel lowerPanel = new ScrollPanel();
	private HorizontalPanel lowerSentPanel = new HorizontalPanel();
	private ArrayList<Button> sourceWords = new ArrayList<Button>();

	// Lower-Middle part, space 
	private HorizontalPanel lowerSpace = new HorizontalPanel();
	private Label selectedEntry = new Label();
	private Label debugEntry = new Label();
	//private FlexTable alignedFlexTable = new FlexTable();
	
	// Bottom part, a set of controls
	private HorizontalPanel controls = new HorizontalPanel();
	private Button drawButton = new Button("Draw");
	private Button postButton = new Button("Submit");
	private TextBox newStringTB = new TextBox();
	private String assignmentId;
	
	// Handler
	private AlignerEventHandler handler = new AlignerEventHandler();
	
	// Sentence alignment
	private SentenceAlignment alignment;
	// And the words need to be aligned
	private ArrayList<AltAlignerTab> tabs = new ArrayList<AltAlignerTab>();
	private ArrayList<Integer> dx1;
	
	private int selectedTgtIndex = -1, selectedSrcIndex = -1;
	@Override
	public void onModuleLoad() {
		
		if(getUserAgent().contains("msie"))
		{
			titleLabel.setText("Internet Explorer is NOT SUPPORTED, please use firefox/chrome/safari");
			titleLabel.addStyleName("title-info-error");
			RootPanel.get().add(titleLabel);
			return;
		}
		// Parse the parameters
		String srcSent = Window.Location.getParameter("srcsent");
		String tgtSent = Window.Location.getParameter("tgtsent");
		String idxOnSource = Window.Location.getParameter("idxonsrc");
		boolean idxOs = false;
		String idx  = Window.Location.getParameter("idx");
		assignmentId  = Window.Location.getParameter("assignmentId");
		
		
		if(assignmentId == null || srcSent == null || tgtSent == null || idxOnSource==null || idx==null){
			titleLabel.addStyleName("title-info-error");
			titleLabel.setText("The URL is invalid, what you see below is just a demo");
			try {
				alignment = new SentenceAlignment("二零零五年 的 夏天 , 一 个 被 人们 期待已久 的 画面 开始 在 香港 的 各 大 媒体 频繁 出现 ,","in the summer of $num_(2005) , a picture that people have long been looking forward to started emerging with frequency in various major hong kong media .","");
				idx = "0_3_4";
				assignmentId = "123";
			} catch (Exception e) {
				
			}
		}else{
			
			try {
				idxOs = Boolean.parseBoolean(idxOnSource);
				if(idxOs)
					alignment = new SentenceAlignment(srcSent,tgtSent,"");
				else
					alignment = new SentenceAlignment(tgtSent,srcSent,"");
			} catch (Exception e) {
				titleLabel.setText("The URL is invalid, what you see below is just a demo ");
				titleLabel.addStyleName("title-info-error");
			}
			titleLabel.addStyleName("title-info");
			titleLabel.setText("Label translation of several words ");
		}
		
		taskPanel.add(titleLabel2);
		// With the indices, create the tabs
		String[] dx = idx.split("\\_");
		dx1 = new ArrayList<Integer>();
		int i = 0;
		for(String s : dx){
			final int j = i;
			if(s.trim().length()>0){
				int widx = Integer.parseInt(s);
				dx1.add(widx);
//				final AltAlignerTab alTab = new AltAlignerTab();
//				alTab.setIndexOnSource(idxOs);
//				alTab.setRootPanel(RootPanel.get());
//				alTab.setWordIndex(widx);
//				alTab.setSent(alignment);
//				tabs.add(alTab);
//				Button btn = new Button(alignment.getSource(widx));
//				//btn.setStylePrimaryName("unlabeled-button");
//				btn.addClickHandler(new ClickHandler(){
//
//					@Override
//					public void onClick(ClickEvent event) {
//						selectedSrcIndex = alTab.getWordIndex();
//						alignment = alTab.getSent();
//						buttonClicked(sourceWords.get(alTab.getWordIndex()));
//					}
//					
//				});
//				taskButtons.add(btn);
//				taskPanel.add(btn);
			}
		}
		
		
		Date d = new Date();
		d.setYear(d.getYear()+1);
		final Date cookieExp = d; 
		
		
		handler.setApp(this);
		refreshSentence();
		/** Initialize the information panel **/
		infoPanel.add(titleLabel);
		infoPanel.add(titlehelp);
		
		String descPanelVisible = Cookies.getCookie("descPanelVisible");
		if(descPanelVisible==null || !descPanelVisible.equalsIgnoreCase("FALSE") ){
			descPanel.setVisible(true);
			titlehelp.setText(closeHelp);
		}else{
			descPanel.setVisible(false);
			titlehelp.setText(openHelp);
		}
		titlehelp.setWidth("500px");
		titlehelp.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				descPanel.setVisible(!descPanel.isVisible());
				Cookies.setCookie("descPanelVisible", descPanel.isVisible()?"TRUE":"FALSE",cookieExp);
				titlehelp.setText(descPanel.isVisible()?closeHelp:openHelp);
			}
			
		});
		
		
		
		descPanel.add(descHTML);
		descPanel.addStyleName("desc-panel-info");
		
		descHTML.setHTML(setupDescription());
		
		
		infoPanel.add(descPanel);
		
		
		String demoPanelVisible = Cookies.getCookie("demoPanelVisible");
		if(demoPanelVisible==null || !demoPanelVisible.equalsIgnoreCase("TRUE") ){
			demoPanel.setVisible(false);
			titledemo.setText(openDemo);
		}else{
			demoPanel.setVisible(true);
			titledemo.setText(closeDemo);
		}
		
		infoPanel.add(titledemo);
		titledemo.setWidth("500px");
		titledemo.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				demoPanel.setVisible(!demoPanel.isVisible());
				titledemo.setText(demoPanel.isVisible()?closeDemo:openDemo);
				Cookies.setCookie("demoPanelVisible", demoPanel.isVisible()?"TRUE":"FALSE",cookieExp);
			}
			
		});
		demoPanel.add(demoHTML);
		demoPanel.addStyleName("desc-panel-demo");
		demoHTML.setHTML("<img src=\"Demo.png\" width=\"900px\"/>");
		infoPanel.add(demoPanel);
		infoPanel.add(taskPanel);
		infoPanel.add(statusLabel);
		
		
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.add(infoPanel);
		mainPanel.add(decPanel);
		
		/** Initialize the top panel **/
		
		upperPanel.add(upperSentPanel);
		
		// Refresh
		upperPanel.addScrollHandler(new ScrollHandler(){
			@Override
			public void onScroll(ScrollEvent event) {
				refreshConnections();
			}			
		});
		
		tgtPanel.add(upperPanel);
		
		mainPanel.add(tgtPanel);
		upperPanel.setWidth(""+Window.getClientWidth()+"px");
		/** Initialize the middle panel (Spacing) **/
		
		middleSpace.setCoordSize(Window.getClientWidth(), Window.getClientHeight());
		middleSpace.setWidth(""+Window.getClientWidth()+"px");
		middleSpace.setHeight("150px");
		mainPanel.add(middleSpace);
		
		/** Initialize the lower panel **/

		lowerPanel.add(lowerSentPanel);
		lowerPanel.addScrollHandler(new ScrollHandler(){
			@Override
			public void onScroll(ScrollEvent event) {
				refreshConnections();
			}			
		});
		srcPanel.add(lowerPanel);
		mainPanel.add(srcPanel);
		lowerPanel.setWidth(""+Window.getClientWidth()+"px");
		
		/** Initialize the lower-middle panel (Spacing) **/
		lowerSpace.setHeight("120px");
		//lowerSpace.add(selectedEntry);
		//lowerSpace.add(debugEntry);
		//lowerSpace.add(alignedFlexTable);
		//alignedFlexTable.addStyleName("cw-FlexTable");
		//debugEntry.setText(Window.Location.getParameter("srcsent"));
		
		//mainPanel.add(lowerSpace);
		
		/** Initialize the control panel**/
		//controls.add(newStringTB);
		//controls.add(drawButton);
		controls.add(postButton);
		
		decPanel = new DecoratorPanel();
		decPanel.add(controls);
		mainPanel.add(decPanel);
		
		// See if it has been accepted
		
		if(assignmentId.equals("ASSIGNMENT_ID_NOT_AVAILABLE")){
			postButton.setEnabled(false);
			postButton.setText("You have not yet accept, the HIT is in preview mode");
		}
		
		// Add event handlers
		drawButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String txt = newStringTB.getText();
				try {
					SentenceAlignment alm = new SentenceAlignment(txt);
					alignment = alm;
				} catch (Exception e) {
					Window.alert("The alignment format is illegal");
					return;
				}
				refreshSentence();
				selectedSrcIndex=-1;
				refreshConnections();
				refreshWordSelection();
			}
		});
		
		postButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				validateAndSubmit();
			}
			
		});
		
		Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event) {
				lowerPanel.setWidth(""+Window.getClientWidth()+"px");
				upperPanel.setWidth(""+Window.getClientWidth()+"px");
				middleSpace.setCoordSize(Window.getClientWidth(), Window.getClientHeight());
				middleSpace.setWidth(""+Window.getClientWidth()+"px");
				titleLabel.setWidth(""+(Window.getClientWidth()-124)+"px");
				descPanel.setWidth(""+(Window.getClientWidth()-124)+"px");
				descHTML.setWidth(""+(Window.getClientWidth()-124)+"px");
				refreshConnections();
			}
			
		});
		
		
		RootPanel.get("alignerMain").add(mainPanel);
		lowerPanel.setWidth(""+Window.getClientWidth()+"px");
		upperPanel.setWidth(""+Window.getClientWidth()+"px");
		middleSpace.setCoordSize(Window.getClientWidth(), Window.getClientHeight());
		middleSpace.setWidth(""+Window.getClientWidth()+"px");
		descPanel.setWidth(""+(Window.getClientWidth()-124)+"px");
		descHTML.setWidth(""+(Window.getClientWidth()-124)+"px");
		titleLabel.setWidth(""+(Window.getClientWidth()-124)+"px");
		refreshConnections();
	}
	
	protected void validateAndSubmit() {
		// TODO: Serialize the alignment and send it back
		StringBuffer postData = new StringBuffer();
		postData.append("http://workersandbox.mturk.com/mturk/externalSubmit?");
		StringBuffer align = new StringBuffer();
		Iterator<AlignmentLink> it = alignment.iterator();
		while(it.hasNext()){
			AlignmentLink a = it.next();
			align.append(a.getSourceIndex()).append("-");
			if(a.getTargetIndex()>alignment.getSourceLength())
				align.append(0).append(" ");
			else
				align.append(a.getTargetIndex()).append(" ");
		}
		String res = align.toString().trim();
		postData.append(URL.encode("aln")).append("=").append(URL.encode(res));
		postData.append("&");
		postData.append(URL.encode("assignmentId")).append("=").append(URL.encode(assignmentId));
		Window.open(postData.toString(), "_self", "");
		//Window.alert(postData.toString());
		
	}

	public void refreshSentence(){
		for(Button b : targetWords){
			upperSentPanel.remove(b);
		}
		targetWords.clear();
		for(Button b : sourceWords){
			lowerSentPanel.remove(b);
		}
		sourceWords.clear();
		
		for(int i = 0 ; i < alignment.getTargetLength(); i++){
			Button newB = new Button(alignment.getTarget(i));
			newB.setStylePrimaryName("targetSentence");
			upperSentPanel.add(newB);
			newB.addMouseOverHandler(handler);
			newB.addMouseOutHandler(handler);
			newB.addClickHandler(handler);
			targetWords.add(newB);
		}
		
		{
			Button newB = new Button("(EMPTY WORD)");
			newB.setStylePrimaryName("targetSentence");
			upperSentPanel.add(newB);
			newB.addMouseOverHandler(handler);
			newB.addMouseOutHandler(handler);
			newB.addClickHandler(handler);
			targetWords.add(newB);
		}

		
		for(int i = 0 ; i < alignment.getSourceLength(); i++){
			Button newB = new Button(alignment.getSource(i));
			if(dx1.contains(i)){
				if(alignment.getTargetIndices(i+1).length>0)
					newB.setStylePrimaryName("labelSourceSentence");
				else
					newB.setStylePrimaryName("unlabelSourceSentence");
			}else
				newB.setStylePrimaryName("sourceSentence");
			lowerSentPanel.add(newB);
			newB.addMouseOverHandler(handler);
			newB.addMouseOutHandler(handler);
			newB.addClickHandler(handler);
			sourceWords.add(newB);
		}
	}
	
	private void drawSingleLine(int i, int j){
		Button bsrc = sourceWords.get(i-1);
		Button btgt = targetWords.get(j-1);
		
		int srcLeft = bsrc.getAbsoluteLeft();
		int srcWidth = bsrc.getOffsetWidth();
		int tgtLeft = btgt.getAbsoluteLeft();
		int tgtWidth = btgt.getOffsetWidth();
		int myLeft = middleSpace.getAbsoluteLeft();
		
		// Draw a line
		
		
		middleSpace.setLineWidth(1);
		
		if(selectedSrcIndex == i-1){
			middleSpace.setStrokeStyle(Color.BLUEVIOLET);
			middleSpace.setFillStyle(Color.BLUEVIOLET);
		}
		else{
			middleSpace.setStrokeStyle(Color.GREEN);
			middleSpace.setFillStyle(Color.GREEN);
		}
		
		middleSpace.beginPath();
		
		double angle = Math.abs((srcLeft+srcWidth/2)- tgtLeft+tgtWidth/2) / (double)middleSpace.getCoordHeight();
		int width = (int)(2.0+angle*8); 
		middleSpace.moveTo(srcLeft+srcWidth/2-width, middleSpace.getCoordHeight());
		middleSpace.lineTo(tgtLeft+tgtWidth/2-width, 0);
		middleSpace.lineTo(tgtLeft+tgtWidth/2+width, 0);
		middleSpace.lineTo(srcLeft+srcWidth/2+width, middleSpace.getCoordHeight());
		middleSpace.closePath();
		
		//middleSpace.quadraticCurveTo((srcLeft+myLeft+srcWidth/2+tgtLeft+myLeft+tgtWidth/2)/2-15, middleSpace.getCoordHeight()/2, tgtLeft+myLeft+tgtWidth/2, 0);
		middleSpace.stroke();
		
		middleSpace.fill();
	}
	
	public void refreshConnections(){
		middleSpace.clear();
		Iterator<SentenceAlignment.AlignmentLink> it = alignment.iterator();
		while(it.hasNext()){
			AlignmentLink a = it.next();
			drawSingleLine(a.getSourceIndex(),a.getTargetIndex());
		}
		
		int unlabeled = 0,labeled = 0;
		StringBuffer bf1 = new StringBuffer();
		StringBuffer bf2 = new StringBuffer();
		for(int i: dx1){
			if(alignment.getTargetIndices(i+1).length>0){
				bf1.append(alignment.getSource(i)).append(",");
				labeled++;
			}else{
				bf2.append(alignment.getSource(i)).append(",");
				unlabeled ++;
			}
		}
		StringBuffer bf3 = new StringBuffer();
		if(labeled >0){
			bf3.append("You have labeled ").append(labeled).append(" words (").append(bf1.toString()).append("),");
		}else{
			bf3.append("You have not labeled any word, ");
		}
		
		if(assignmentId.equals("ASSIGNMENT_ID_NOT_AVAILABLE")){
			postButton.setEnabled(false);
			postButton.setText("You have not yet accept, the HIT is in preview mode");
		}else
		if(unlabeled >0){
			bf3.append(" and you need to label ").append(unlabeled).append(" words (").append(bf2.toString()).append(") to complete the task");
			postButton.setText("Finish all the words ");
			postButton.setEnabled(false);
		}else{
			bf3.append(" and you are done, check you answer before click on submit.");
			postButton.setText("Submit");
			postButton.setEnabled(true);
		}
		titleLabel2.setText(bf3.toString());
	}
	
	public void refreshWordSelection(){
		if(selectedSrcIndex < 0){
			//selectedEntry.setText("No word selected");
			//alignedFlexTable.clear();
			//alignedFlexTable.removeAllRows();
		}else{
			String srcWord = alignment.getSource(selectedSrcIndex);
			//selectedEntry.setText("Selected word: " +srcWord  + " (" + (selectedSrcIndex+1) + ")" );
			//alignedFlexTable.clear();
			//alignedFlexTable.removeAllRows();
			//Iterator<SentenceAlignment.AlignmentLink> it = alignment.iterator();
			//int i =0;
			//wh/ile(it.hasNext()){
				//final AlignmentLink a = it.next();
				//if(a.getSourceIndex() == selectedSrcIndex+1){
					//alignedFlexTable.setText(i, 0,srcWord);
					//alignedFlexTable.setText(i, 1,"-->");
					//alignedFlexTable.setText(i, 2, alignment.getTarget(a.getTargetIndex()-1));
					//Button bt = new Button("x");
					//bt.addClickHandler(new ClickHandler(){
					//	@Override
					//	public void onClick(ClickEvent event) {
					//		alignment.removeLink(a.getSourceIndex(), a.getTargetIndex());
					//		refreshConnections();
					//		refreshWordSelection();
					//	}
					//});
					//alignedFlexTable.setWidget(i, 3, bt);
				//}
				//i++;
			//}
			
		}
	}
//	|| bt.getStylePrimaryName().equals("sourceSentence")
//	|| bt.getStylePrimaryName().equals("selectedTargetSentence")
//	|| bt.getStylePrimaryName().equals("selectedSourceSentence")
	
	public void buttonClicked(Button bt){
	if(bt.getStylePrimaryName().equals("targetSentence")||bt.getStylePrimaryName().equals("selectedTargetSentence")){
		if(selectedSrcIndex>=0){
			int i = 0;
			selectedTgtIndex = -1;
			for(Button b : targetWords){
				if(b==bt){
					selectedTgtIndex = i;
				}
				i++;
			}
			if(selectedTgtIndex>=0&&selectedTgtIndex < targetWords.size()-1 /*Not empty word*/){
				if(alignment.hasAlignment(selectedSrcIndex+1,selectedTgtIndex+1)){
					alignment.removeLink(selectedSrcIndex+1,selectedTgtIndex+1);
					targetWords.get(selectedTgtIndex).setStylePrimaryName("targetSentence");
				}else{
					alignment.addLink(selectedSrcIndex+1,selectedTgtIndex+1);
					alignment.removeLink(selectedSrcIndex+1, targetWords.size());
					targetWords.get(targetWords.size()-1).setStylePrimaryName("targetSentence");
					targetWords.get(selectedTgtIndex).setStylePrimaryName("selectedTargetSentence");
				}
				
				refreshConnections();
				refreshWordSelection();
			}else if (selectedTgtIndex == targetWords.size()-1){
				for(int j = 0 ; j < targetWords.size()-1; j++){
					if(alignment.hasAlignment(selectedSrcIndex+1, j+1)){
						alignment.removeLink(selectedSrcIndex+1, j+1);
						targetWords.get(j).setStylePrimaryName("targetSentence");
					}
				}
				targetWords.get(targetWords.size()-1).setStylePrimaryName("selectedTargetSentence");
				alignment.addLink(selectedSrcIndex+1, targetWords.size());
				refreshConnections();
				refreshWordSelection();
			}
		}
	}else if(bt.getStylePrimaryName().indexOf("labelSourceSentence")>=0){
			int i = 0;
			selectedSrcIndex = -1;
			for(Button b : sourceWords){
				if(dx1.contains(i)){
					if(alignment.getTargetIndices(i+1).length>0)
						b.setStylePrimaryName("labelSourceSentence");
					else
						b.setStylePrimaryName("unlabelSourceSentence");
				}else
					b.setStylePrimaryName("sourceSentence");
				if(b==bt){
					if(dx1.contains(i))
						selectedSrcIndex = i;
				}
				i++;
			}
			if(selectedSrcIndex<0){
				refreshWordSelection();
				refreshConnections();
				return;
			}
			bt.setStylePrimaryName("selectedSourceSentence");
			for(Button b : targetWords){
				b.setStylePrimaryName("targetSentence");
			}
			int[] als = alignment.getTargetIndices(selectedSrcIndex+1);
			int leftMost = 100000;
			int rightMost = -1;
			for(int al : als){
				targetWords.get(al-1).setStylePrimaryName("selectedTargetSentence");
				int tgtLeft = targetWords.get(al-1).getAbsoluteLeft();
				int tgtWidth = targetWords.get(al-1).getOffsetWidth();
				int tgtRight = tgtLeft + tgtWidth;
				if(leftMost > tgtLeft)
					leftMost = tgtLeft;
				if(tgtRight > rightMost)
					rightMost = tgtRight;
			}
			int pos = upperPanel.getHorizontalScrollPosition();
			int center = upperPanel.getOffsetWidth()/2;
			if(rightMost > 0){
				upperPanel.setHorizontalScrollPosition(Math.max(0,pos - center + leftMost));
			}
			
		}else if(bt.getStylePrimaryName().equals("selectedSourceSentence")){
			int i = 0;
			for(Button b : sourceWords){
				if(b==bt){
					if(alignment.getTargetIndices(i+1).length>0){
						b.setStylePrimaryName("labelSourceSentence");
					}else{
						b.setStylePrimaryName("unlabelSourceSentence");
					}
				}
				i++;
			}
			selectedSrcIndex = -1;
			for(Button b : targetWords){
				b.setStylePrimaryName("targetSentence");
			}
		}
		refreshWordSelection();
		refreshConnections();
	}
	
	public void setSentence(SentenceAlignment sent){
		this.alignment = sent;
		refreshConnections();
		refreshWordSelection();
	}
	
//	public String setupDescription(){
//		StringBuffer bf = new StringBuffer();
//		bf.append("<p>In this task we present you two sentences, which are translation of each other, the sentence you see in the bottom part of the screen, reads </p>");
//		bf.append("<pre>").append(alignment.getSource()).append("</pre>");
//		bf.append("<p>We call it <b><u>source sentence</u></b>, and above it we see the \" <b><u>target sentence</u></b>\", reads");
//		bf.append("<pre>").append(alignment.getTarget()).append("</pre>");
//		bf.append("<p>We want to know which word in the <b><u>target sentence</u></b> is the translation of each of the following words in <b><u>source sentence:</u></b> (marked in gold)</p> ");
//		bf.append("<p>");
//		for(int i = 0 ; i < alignment.getSourceLength() ; i++){
//			if(dx1.contains(i)){
//				bf.append("<font color=\"#FFFFFF\" style=\"background-color:#fd8c28\"><u>").append(alignment.getSource(i)).append("</u></font> ");
//			}else{
//				bf.append(alignment.getSource(i)).append(" ");
//			}
//		}
//		bf.append("</p>");
//		bf.append("<p> As you can see right below this box, there are ").append(dx1.size()).append(" buttons, you can click on each of them to select the word to label, and they will display in <span style=\"background-color:#CC3333;color:#FFFFFF\">red background</span>. Now you can click on the <b><u>target word</u></b> that is the translation of the word, the font of the word you click will become <font color=\"#DD00DD\"> pink </font> which indicates it has been selected.");
//		bf.append("If you are using Firefox or Chrome/Safari, you will also see a line being drawn between the two words.<b> select as many word as you want if the source word translate to a more than one targe word. </b> </p> <p> Important: If no target word is the translation of the source word, you need to select the <b> empty word </b> in the end of the target sentence. ");
//		bf.append("When a source word is labeled, the corresponding button will be checked, and the submit button will be enabled <b>when all required words are labeled.</b>");
//		bf.append(" Labeled word will display in <font color=\"#FFFFFF\" style=\"background-color:#1a4f68\">dark blue</font>.</p>");
//		bf.append(" <p> Feel free to play with the interface before you do actual labeling, it is quite intuitive </p>");
//		return bf.toString();
//	}
	
	public String setupDescription(){
		StringBuffer bf = new StringBuffer();
		bf.append("<p> In the task we show you two sentence that are translation of each other, and what you need to do"
				+ " is link words that are translation of each other."+ 
				"<p>The words we want you to label are in gold background, and you need to label these words"+
				" before you can submit the task.</p>"+ 
				"<p>To add a link, first click on the Chinese word with gold background and then click on English words above it"+
				" that are translation of it, a link between them will show up. Click the English word again to deselect it.</p>"+
				"<p>If you cannot find translation of a Chinese word, click on \"EMPTY WORD\" which is always the last English word.</p>"+
				"<p>Please note Chinese words with gold background must all be labeled, and it can be cases that <b>multiple English words are translated to one Chinese word, please label all these words</b></p>"+
				"<p>We evaluate your submission, if 30% of your answer are different from other users, your submission will be rejected.</p>"+
				"<p><i>Click the button below to show graphical instruction of usage.</i></p>");
		return bf.toString();
	}
	
	public static native String getUserAgent() /*-{
	return navigator.userAgent.toLowerCase();
	}-*/;

}
