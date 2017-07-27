package com.jzsec.modules.schema.web;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.schema.entity.Schema;
import com.jzsec.modules.schema.entity.SchemaField;
import com.jzsec.modules.schema.service.SchemaFieldService;
import com.jzsec.modules.schema.service.SchemaService;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 数据源格式配置[XP指令]
 * @author 劉 焱
 * @date 2016-11-24
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/schema")
public class SchemaController extends BaseController {
	@Autowired
	private SchemaService schemaService;
	@Autowired
	private SchemaFieldService schemaFieldService;

	/**
	 * Schema列表
	 * @param schema
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("rtc:schema:list")
	@RequestMapping(value = {"list", ""})
	public String list(Schema schema, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Schema> page = schemaService.findPage(new Page<Schema>(request, response), schema);
		model.addAttribute("page", page);
		return "modules/schema/schema";
	}

	/**
	 * 字段列表
	 * @param schemaField
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("rtc:schema:field")
	@RequestMapping(value = "field")
	public String getSchemaField(SchemaField schemaField, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchemaField> p = new Page<SchemaField>(request, response);
		p.setCount(100);
		p.setPageSize(100);
		Page<SchemaField> page = schemaFieldService.findPage(p, schemaField);
		model.addAttribute("page", page);
		return "modules/schema/field";
	}

	/**
	 * 指令详情/编辑页
	 * @param schema
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"rtc:schema:add","rtc:schema:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String editSchema(Schema schema, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(schema.getId() != null) {
			Schema sc = schemaService.findUniqueByProperty("id", schema.getId());
			SchemaField schemaField = new SchemaField();
			schemaField.setSchemaId(sc.getSchemaId());
			List<SchemaField> schemaFields = schemaFieldService.findList(schemaField);
			sc.setSchemaFieldList(schemaFields);
			model.addAttribute("schema", sc);
			return "modules/schema/schemaForm";
		}
		return "modules/schema/schemaForm";
	}

	/**
	 * 删除字段
	 * @param schemaField
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"rtc:schema:add","rtc:schema:edit"},logical=Logical.OR)
	@RequestMapping(value = "deleteField")
	@ResponseBody
	public String deleteField(SchemaField schemaField, HttpServletRequest request, HttpServletResponse response, Model model) {
		schemaFieldService.delete(schemaField);
		
		Schema schema = new Schema();
		schema.setIsNewRecord(false);
		schema.setSchemaId(schemaField.getSchemaId());
		try {
			schemaService.PushZookeeper(schema, false);
		} catch (Exception e) {
			try {
				schemaService.PushZookeeper(schema, false);
			} catch (Exception e1) {
				e1.printStackTrace();
				return "error";
			}
		}
		
		return "ok";
	}

	/**
	 * 保存字段, 暂未使用
	 * @param schemaField
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveField")
	@ResponseBody
	public Object saveField(SchemaField schemaField, HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean isNew = schemaField.getId() == null ? true : false;
		try {
			schemaField.setFieldDescribe(URLDecoder.decode(schemaField.getFieldDescribe(),   "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		schemaFieldService.save(schemaField);
		if (isNew){
			schemaField = schemaFieldService.findUniqueByProperty("field_name", schemaField.getFieldName());
		}
		
		Schema schema = new Schema();
		schema.setIsNewRecord(false);
		schema.setSchemaId(schemaField.getSchemaId());
		try {
			schemaService.PushZookeeper(schema, false);
		} catch (Exception e) {
			try {
				schemaService.PushZookeeper(schema, false);
			} catch (Exception e1) {
				e1.printStackTrace();
				return "error";
			}
		}
		
		return schemaField;
	}

	/**
	 * 保存
	 * @param schema
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"rtc:schema:add","rtc:schema:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Schema schema, Model model, RedirectAttributes redirectAttributes) {
		schemaService.saveSchema(schema);

		try {
			schemaService.PushZookeeper(schema, false);
		} catch (Exception e) {
			try {
				schemaService.PushZookeeper(schema, false);
			} catch (Exception e1) {
				addMessage(redirectAttributes, "信息数据库保存成功, 但推送至zookeeper节点异常!");
				e1.printStackTrace();
			}
		}
		
		return "redirect:" + adminPath + "/rtc/schema/list";
	}

	/**
	 * 删除
	 * @param schema
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rtc:schema:del")
	@RequestMapping(value = "delete")
	public String deleteAll(Schema schema, RedirectAttributes redirectAttributes) {
		schemaService.deleteSchema(schema);
		
		try {
			schemaService.PushZookeeper(schema, true);
		} catch (Exception e) {
			try {
				schemaService.PushZookeeper(schema, true);
			} catch (Exception e1) {
				addMessage(redirectAttributes, "信息数据库删除成功, 但推送至zookeeper节点异常!");
				e1.printStackTrace();
			}
		}
		
		return "redirect:" + adminPath + "/rtc/schema/list";
	}
}