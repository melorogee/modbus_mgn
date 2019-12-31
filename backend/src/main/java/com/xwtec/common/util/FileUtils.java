package com.xwtec.common.util;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.URLEncoder;


public class FileUtils {
		/**
		 * 下载文件时，针对不同浏览器，进行附件名的编码
		 *
		 * @param filename
		 *            下载文件名
		 * @param agent
		 *            客户端浏览器
		 * @return 编码后的下载附件名
		 * @throws IOException
		 */
		public static String encodeDownloadFilename(String filename, String agent)
				throws IOException {
			if (agent.contains("Firefox")) { // 火狐浏览器
				filename = "=?UTF-8?B?"
						+ new BASE64Encoder().encode(filename.getBytes("utf-8"))
						+ "?=";
				filename = filename.replaceAll("\r\n", "");
			} else { // IE及其他浏览器
				filename = URLEncoder.encode(filename, "utf-8");
				filename = filename.replace("+"," ");
			}
			return filename;
		}

	/**
	 * 验证是否是图片
	 *
	 * @param tmpName
	 * @return
	 * @throws Exception
	 */
	public static boolean isPicture(String tmpName) throws Exception {
		// 声明图片后缀名数组
		String imgeArray[] = {"bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico"};
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否是声频
	 *
	 * @param tmpName
	 * @return MP3
	 * @throws Exception
	 */
	public static boolean isAudio(String tmpName) throws Exception {
		// 声明图片后缀名数组
		String imgeArray[] = {"mp3"};
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否是视频
	 *
	 * @param tmpName
	 * @return
	 * @throws Exception
	 */
	public static boolean isVideo(String tmpName) throws Exception {
		// 声明图片后缀名数组
		String imgeArray[] = {"avi", "mov", "rmvb", "rm", "flv", "mp4", "3gp"};
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否是office
	 *
	 * @param tmpName
	 * @return
	 * @throws Exception
	 */
	public static boolean isOffice(String tmpName) throws Exception {
		// 声明图片后缀名数组
		String imgeArray[] = {"doc", "docx", "xls", "ppt", "xlsx"};
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
