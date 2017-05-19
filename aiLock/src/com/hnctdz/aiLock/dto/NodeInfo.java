package com.hnctdz.aiLock.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.domain.system.SysRes;

/**
 * @ClassName NodeInfo.java
 * @Author WangXiangBo
 */
public class NodeInfo {
	private String id;

	private String text;

	private String iconCls = null;

	private String checked;

	private Long order;

	private String attributes;

	private String url;

	private String parentId;

	private List<NodeInfo> children;

	public NodeInfo() {
	}

	public NodeInfo(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public NodeInfo(String id, String text, String checked, String attributes,
			String url, String parentId) {
		this.id = id;
		this.text = text;
		this.checked = checked;
		this.attributes = attributes;
		this.url = url;
		this.parentId = parentId;
	}
	
	//把系统资源菜单List数据换转成树节点格式List
	public static List<NodeInfo> getNodeChildren(List<SysRes> list) {
		List<NodeInfo> nodeList = new ArrayList<NodeInfo>();
		Map<Long, NodeInfo> map = new HashMap<Long, NodeInfo>();
		disposeNoShiftNodeList(nodeList, map, list);
		return nodeList;
	}
	
	//生成系统资源菜单数据树节点数据
	private static List<NodeInfo> disposeNoShiftNodeList(List<NodeInfo> nodeList, Map<Long, NodeInfo> map, List<SysRes> sysResList){
		Long preId = null;
		NodeInfo tempNode = null;
		NodeInfo node = null;
		
		List<SysRes> noShiftNodeList = new ArrayList<SysRes>();
		for(SysRes res : sysResList){
			tempNode = null;
			preId = res.getResParentId();
			//上级不为null，且上级不是本身
			if (preId != null && !res.getResId().equals(res.getResParentId())) {
				tempNode = map.get(preId);
				if(tempNode == null){
					noShiftNodeList.add(res);
					continue;
				}
			}
			node = new NodeInfo();
			node.setId(res.getResId().toString());
			node.setText(res.getResName());
			node.setUrl(res.getResUrl());
			node.setIconCls(res.getResIcon());
			node.setParentId(preId != null ? preId.toString() : "");
			node.setOrder(res.getResOrder());
			
			if(tempNode == null){
				nodeList.add(node);
			} else {
				tempNode.getChildren().add(node);
			}
			map.put(res.getResId(), node);
		}
		
		if(noShiftNodeList.size() > 0){
			disposeNoShiftNodeList(nodeList, map, noShiftNodeList);
		}else{
			nodeSort(nodeList);
		}
		return nodeList;
	}
	
	//树节点数据排序
	public static void nodeSort(List<NodeInfo> nodeList){
		for(NodeInfo nodeInfo: nodeList){
			if(nodeInfo.getChildren() != null){
				nodeSort(nodeInfo.getChildren());
			}
			List<NodeInfo> Children =  nodeInfo.getChildren();
			Collections.sort(Children,new Comparator<NodeInfo>(){
				public int compare(NodeInfo node1, NodeInfo node2) {
					return node1.getOrder().compareTo(node2.getOrder());
				}
			});
		}
	}
	
	//把组织架构List数据换转成树节点格式List
	public static List<NodeInfo> getOrgInfoToNode(List<OrgInfo> list) {
		List<NodeInfo> nodeList = new ArrayList<NodeInfo>();
		Map<String, NodeInfo> map = new HashMap<String, NodeInfo>();
		spanningOrgInfoTreeData(nodeList, map, list);
		return nodeList;
	}
	
	//生成组织架构树节点数据
	private static List<NodeInfo> spanningOrgInfoTreeData(List<NodeInfo> nodeList, Map<String, NodeInfo> map, List<OrgInfo> orgInfoList){
		String preId = null;
		NodeInfo tempNode = null;
		NodeInfo node = null;
		
		List<OrgInfo> noShiftNodeList = new ArrayList<OrgInfo>();
		for(OrgInfo org : orgInfoList){
			tempNode = null;
			preId = org.getOrgParentId() == null ? null : org.getOrgParentId().toString();
			//上级不为null，且上级不是本身
			if (preId != null && !org.getOrgParentId().equals(org.getOrgId())) {
				tempNode = map.get(preId);
				if(tempNode == null){
					boolean bl = false;
					for(OrgInfo org2 : orgInfoList){
						if(org2.getOrgId().equals(org.getOrgParentId())){
							noShiftNodeList.add(org);
							bl = true;
							break;
						}
					}
					if(bl){
						continue;
					}
				}
			}
			node = new NodeInfo();
			node.setId(org.getOrgId().toString());
			node.setText(org.getOrgName());
			node.setParentId(preId);
			
			if(tempNode == null){
				nodeList.add(node);
			} else {
				tempNode.getChildren().add(node);
			}
			map.put(node.getId(), node);
		}
		
		if(noShiftNodeList.size() > 0){
			spanningOrgInfoTreeData(nodeList, map, noShiftNodeList);
		}
		return nodeList;
	}

	public static List<NodeInfo> getSysAreaToNode(List<SysArea> list) {
		List nodeList = new ArrayList();
		Map map = new HashMap();
		spanningSysAreaTreeData(nodeList, map, list);
		return nodeList;
	}

	private static List<NodeInfo> spanningSysAreaTreeData(List<NodeInfo> nodeList, Map<String, NodeInfo> map, List<SysArea> sysAreaList) {
		String preId = null;
		NodeInfo tempNode = null;
		NodeInfo node = null;

		List noShiftNodeList = new ArrayList();
		for (SysArea area : sysAreaList) {
			tempNode = null;
			preId = area.getParentId() == null ? null : area.getParentId().toString();

			if (preId != null) {
				tempNode = (NodeInfo) map.get(preId);
				if (tempNode == null) {
					boolean bl = false;
					for (SysArea area2 : sysAreaList) {
						if (area2.getAreaId().equals(area.getParentId())) {
							noShiftNodeList.add(area);
							bl = true;
							break;
						}
					}
					if (bl) {
						continue;
					}
				}
			}
			node = new NodeInfo();
			node.setId(area.getAreaId().toString());
			node.setText(area.getAreaName());
			node.setParentId(preId);
			if (tempNode == null){
				nodeList.add(node);
			}else {
				tempNode.getChildren().add(node);
			}
			map.put(node.getId(), node);
		}

		if (noShiftNodeList.size() > 0) {
			spanningSysAreaTreeData(nodeList, map, noShiftNodeList);
		}
		return nodeList;
	}
	
	//把基础数据List数据换转成树节点格式List
	public static List<NodeInfo> getSysBasicDataToNode(List<SysBasicData> list) {
		List<NodeInfo> nodeList = new ArrayList<NodeInfo>();
		Map<String, NodeInfo> map = new HashMap<String, NodeInfo>();
		spanningSysBasicDataTreeData(nodeList, map, list);
		return nodeList;
	}
	
	//生成基础数据树节点数据
	private static List<NodeInfo> spanningSysBasicDataTreeData(List<NodeInfo> nodeList, Map<String, NodeInfo> map, List<SysBasicData> basicDataList){
		String preId = null;
		NodeInfo tempNode = null;
		NodeInfo node = null;
		
		List<SysBasicData> noShiftNodeList = new ArrayList<SysBasicData>();
		for(SysBasicData res : basicDataList){
			tempNode = null;
			preId = res.getParentId() == null ? null : res.getParentId().toString();
			//上级不为null，且上级不是本身
			if (preId != null && !res.getParentId().equals(res.getBasicDataId())) {
				tempNode = map.get(preId);
				if(tempNode == null){
					noShiftNodeList.add(res);
					continue;
				}
			}
			node = new NodeInfo();
			node.setId(res.getBasicDataId().toString());
			node.setText(res.getTypeName());
			node.setParentId(preId);
			
			if(tempNode == null){
				nodeList.add(node);
			} else {
				tempNode.getChildren().add(node);
			}
			map.put(node.getId(), node);
		}
		
		if(noShiftNodeList.size() > 0){
			spanningSysBasicDataTreeData(nodeList, map, noShiftNodeList);
		}
		return nodeList;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<NodeInfo> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<NodeInfo>();
		}
		return this.children;
	}

	public void setChildren(List<NodeInfo> children) {
		this.children = children;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

}
