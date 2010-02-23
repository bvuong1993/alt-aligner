package edu.cmu.lti.atlaligner.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class SentenceAlignment implements Serializable{
	
	public static class AlignmentLink implements Serializable, Comparable<AlignmentLink>{
		public AlignmentLink(int src, int tgt) {
			srcIndex = src;
			tgtIndex = tgt;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + srcIndex;
			result = prime * result + tgtIndex;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AlignmentLink other = (AlignmentLink) obj;
			if (srcIndex != other.srcIndex)
				return false;
			if (tgtIndex != other.tgtIndex)
				return false;
			return true;
		}

		private static final long serialVersionUID = 1L;
		private int srcIndex;
		private int tgtIndex;
		public int getSourceIndex(){
			return srcIndex;
		}
		public int getTargetIndex(){
			return tgtIndex;
		}
		public void setSourceIndex(int idx){
			if(idx < 0 ) idx = 0;
			srcIndex = idx;
		}
		
		public void setTargetIndex(int idx){
			if(idx < 0 ) idx = 0;
			tgtIndex = idx;
		}
		@Override
		public int compareTo(AlignmentLink other) {
			if(srcIndex > other.srcIndex)
				return 1;
			else if (srcIndex < other.srcIndex)
				return -1;
			else if (tgtIndex > other.tgtIndex)
				return 1;
			else if (tgtIndex < other.tgtIndex)
				return -1;
			return 0;
		}
		
		
		
	}

	private static final long serialVersionUID = 1L;
	
	private String[] src;
	private String[] tgt;
	
	TreeSet<AlignmentLink> links = new TreeSet<AlignmentLink>();
	
	public int getSourceLength(){
		return src.length;
	}
	
	public int getTargetLength(){
		return tgt.length;
	}
	
	public String getSource(int i){
		return src[i];
	}
	
	public String getTarget(int i){
		if(i>=tgt.length) return ("(EMPTY WORD)");
		return tgt[i];
	}
	
	public String getSource(){
		String ret = "";
		for(int i = 0; i < src.length;i++){
			ret += src[i] + (i==src.length-1 ? "" : " ");
		}
		return ret;
	}
	
	public String getTarget(){
		String ret = "";
		for(int i = 0; i < tgt.length;i++){
			ret += tgt[i] + (i==tgt.length-1 ? "" : " ");
		}
		return ret;
	}
	
	public void removeLink(int src, int tgt){
		AlignmentLink al = new AlignmentLink(src,tgt);
		links.remove(al);
	}
	
	public void addLink(int src, int tgt){
		AlignmentLink al = new AlignmentLink(src,tgt);
		links.add(al);
	}
	
	public Iterator<AlignmentLink> iterator(){
		return links.iterator();
	}
	
	public SentenceAlignment(String src, String tgt, String al) throws Exception{
		this.src = src.trim().split("\\s+");
		this.tgt = tgt.trim().split("\\s+");
		String[] als = al.trim().split("\\s+");
		for(int i = 0; i< als.length;i++){
			String[] entries = als[i].split("-");
			if(entries.length!=2){
				continue;
			}
			int s = Integer.parseInt(entries[0]);
			int t = Integer.parseInt(entries[1]);
			links.add(new AlignmentLink(s,t));
		}
	}
	
	public SentenceAlignment(String combined) throws Exception{
		String[] cmb = combined.split("\\|\\|\\|");
		if(cmb.length<2){
			throw new Exception();
		}
		this.src = cmb[0].trim().split("\\s+");
		this.tgt = cmb[1].trim().split("\\s+");
		if(src.length==0 || tgt.length==0){
			throw new Exception();
		}
		if(cmb.length>2){
			String[] als = cmb[2].trim().split("\\s+");
			for(int i = 0; i< als.length;i++){
				String[] entries = als[i].split("-");
				if(entries.length!=2){
					continue;
				}
				int s = Integer.parseInt(entries[0]);
				int t = Integer.parseInt(entries[1]);
				links.add(new AlignmentLink(s,t));
			}			
		}
	}
	
	public int[] getTargetIndices(int src){
		ArrayList<Integer> idx  = new ArrayList<Integer>();
		for(AlignmentLink al : links){
			if(al.getSourceIndex()==src){
				idx.add(al.getTargetIndex());
			}
		}
		int i =0;
		int[] ret = new int[idx.size()];
		for(Integer t : idx){
			ret[i++] = t.intValue();
		}
		return ret;
	}
	
	public boolean hasAlignment(int src, int tgt){
		return links.contains(new AlignmentLink(src,tgt));
	}
}
