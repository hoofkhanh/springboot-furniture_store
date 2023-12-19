package com.hokhanh.libary.utils;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

	public static String getSiteUrl(HttpServletRequest httpServletRequest) {
		String siteURL = httpServletRequest.getRequestURL().toString();
		return siteURL.replace(httpServletRequest.getServletPath(), "");
	}
}
