package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName CenterUserController
 * @Description TODO描述
 * @Author wangyue
 * @Date 2020/3/2 15:09
 **/
@Api(value = "center - 用户中心",tags = {"用户中心，user相关接口"})
@RequestMapping("userInfo")
@RestController
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {
        FileOutputStream fileOutputStream = null;
        InputStream fileInputStream = null;
        try {
            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("");
            }
            //定义文件上传路径
//            String fileSpace = IMAGE_USER_FACE_LOCATION;
            String fileSpace = fileUpload.getImageUserFaceLocation();
            //在路径上为每个用户增加一个userId，用于区分每个用户上传
            String uploadPathPrefix = File.separator + userId;
            //开始文件上传
            if (file == null) {
                return IMOOCJSONResult.errorMsg("文件不得为空");
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                return IMOOCJSONResult.errorMsg("文件名不能为空");
            }
            //face-{userId}.png
            String[] fileNameArr = fileName.split("\\.");
            //获取文件的后缀名
            String suffix = fileNameArr[fileNameArr.length - 1];
            //校验文件后缀名，防止被攻击
            if (!suffix.equalsIgnoreCase("png") &&
                    !suffix.equalsIgnoreCase("jpg") &&
                    !suffix.equalsIgnoreCase("jpeg")) {
                return IMOOCJSONResult.errorMsg("图片格式不正确");
            }
            //文件名重组，覆盖式上传，增量式：需要额外拼接当前时间
            String newFileName = "face-" + userId + "." + suffix;
            //上传的头像最终保存的位置
            String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

            File outFile = new File(finalFacePath);
            if (outFile.getParentFile() != null) {
                outFile.getParentFile().mkdirs();
            }
            //文件输出保存到目录
            fileOutputStream = new FileOutputStream(outFile);
            fileInputStream = file.getInputStream();
            IOUtils.copy(fileInputStream, fileOutputStream);
            //获取图片服务地址
            String imageServerUrl = fileUpload.getImageServerUrl();
            //拼接服务器图片访问地址
            String finalUserFaceUrl = imageServerUrl + "/" + userId + "/" + newFileName + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
            Users user = centerUserService.updateUserFace(userId, finalUserFaceUrl);

            user = setNullProperty(user);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
            //TODO 后续要改，增加令牌token，会整合进redis，分布式会话
            return IMOOCJSONResult.ok(user);
        } catch (IOException e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg("头像上传异常");
        }finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        if (result.hasErrors()) {
            Map<String,String> errorMap = getErrors(result);
            return IMOOCJSONResult.errorMap(errorMap);
        }
        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);
        //TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return IMOOCJSONResult.ok(userResult);
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error: errorList) {
            String field = error.getField();
            String errMsg = error.getDefaultMessage();
            map.put(field, errMsg);
        }
        return map;
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
