package org.afc.util;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import lombok.Data;

public class ModelMapperDemo {
	private int seq = 0;


	@Test
	public void testName() throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true);
		modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE);

		SrcObj srcObj = new SrcObj();
		srcObj.groups = Arrays.asList(srcGroup(), srcGroup());

		DestObj destObj = modelMapper.map(srcObj, DestObj.class);

		JUnitUtil.actual(destObj);
	}

	private SrcGroup srcGroup() {
		SrcGroup group = new SrcGroup();
		group.items = Arrays.asList(srcItem(), srcItem());
		return group;
	}

	private SrcItem srcItem() {
		SrcItem item = new SrcItem();
		item.value = "foo" + String.valueOf(++seq);
		return item;
	}
}

@Data
class SrcObj {
	public List<SrcGroup> groups;
}

@Data
class SrcGroup {
	public List<SrcItem> items;
}

@Data
class SrcItem {
	public String value;
}

@Data
class DestObj {
	public List<DestGroup> groups;
}

@Data
class DestGroup {
	public List<DestItem> items;
}

@Data
class DestItem {
	public String value;
}

