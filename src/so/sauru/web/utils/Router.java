package so.sauru.web.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Router extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String packageName;
	private String cPackageName;

	private Class<?> controller;
	private Class<?> scope;
	private String extension = "html";

	Logger logger = LogManager.getLogger("Router");

	public Router() {
		super();
		this.packageName = this.getClass().getPackage().getName();
		this.cPackageName = packageName + ".controller";
	}

	private Class<?> getClazz(String pName, String cName) {
		cName = cName.substring(0, 1).toUpperCase() + cName.substring(1);
		cName = pName + "." + cName;
		try {
			return Class.forName(cName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public class RestRequest {
		private Pattern regexExt = Pattern.compile("(.+)\\.([A-z]+)");
		private Pattern regexPath = Pattern.compile("/([^/?]+)(.*)");
		private HashMap<Integer, String> ctrlMap = new HashMap<Integer, String>();

		private String scopeName = null;
		private String ctrlName = null;
		private String sid = null;
		private String cid = "*";

		public RestRequest(String pathInfo) throws ServletException {
			Matcher matcher;
			String rem = pathInfo;

			matcher = regexExt.matcher(rem);
			if (matcher.find()) {
				extension = matcher.group(2);
				rem = matcher.group(1);
				logger.trace("extension '" + extension + "' provided.");
			} else {
				logger.trace("no extension found. use default.");
			}

			int i = 0;
			while (rem.length() > 0) {
				matcher = regexPath.matcher(rem);
				if (matcher.find()) {
					ctrlMap.put(i, matcher.group(1));
					rem = matcher.group(2);
					logger.trace("get " + matcher.group(1) + ", remind " + rem);
					i++;
				} else {
					break;
				}
			}
			logger.debug("ctrlMap has " + ctrlMap.size() + "elements.");

			switch (ctrlMap.size()) {
			case 4:
				cid = ctrlMap.get(3);
			case 3:
				ctrlName = ctrlMap.get(2);
				sid = ctrlMap.get(1);
				scopeName = ctrlMap.get(0);
				break; // break for 4 and 3. 4 includes 3.
			case 2:
				cid = ctrlMap.get(1);
			case 1:
				ctrlName = ctrlMap.get(0);
				break; // break for 2 and 1. 2 includes 1.
			}

			controller = getClazz(cPackageName, ctrlName);
			scope = getClazz(cPackageName, scopeName);
			if (ctrlName != null && controller != null) {
				if (scopeName == null || scope != null) {
					return;
				} else {
					logger.error("Invalid URI(s) " + pathInfo);
					throw new ServletException("Invalid URI(s) " + pathInfo);
				}
			} else {
				logger.error("Invalid URI(c) " + pathInfo);
				throw new ServletException("Invalid URI(c) " + pathInfo);
			}
		}

		public String getCId() {
			return cid;
		}

		public String getSId() {
			return sid;
		}
	}

	private Object getData(Class<?> ctrl, HashMap<String, String> params) {
		if (ctrl == null) {
			return null;
		} else {
			try {
				Controller o = (Controller) ctrl.newInstance();
				logger.debug("new instance of " + o.getClass().getName());
				return o.index(params.get("id"));
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		out.println("GET request handling");
		out.println("pathinfo: " + req.getPathInfo());
		out.println("params: " + req.getParameterMap());

		try {
			RestRequest resources = new RestRequest(req.getPathInfo());
			HashMap<String, Object> data = new HashMap<String, Object>();
			HashMap<String, String> params = new HashMap<String, String>();
			String cid = resources.getCId();
			String sid = resources.getSId();
			logger.trace("C:" + controller.getSimpleName() + "/" + cid);
			logger.trace("S:" + scope.getSimpleName() + "/" + sid);
			Object o;

			params.put("id", sid);
			params.put("sid", sid);
			o = getData(scope, params);
			if (o instanceof ArrayList) {
				data.put(controller.getSimpleName().toLowerCase(), o);
			} else if (o instanceof HashMap<?, ?>) {
				data.putAll((Map<? extends String, ? extends Object>) o);
			}

			params.put("id", cid);
			params.put("sid", sid);
			o = getData(controller, params);
			if (o instanceof ArrayList) {
				data.put(controller.getSimpleName().toLowerCase(), o);
			} else if (o instanceof HashMap<?, ?>) {
				data.putAll((Map<? extends String, ? extends Object>) o);
			}
			out.println(data);

			if (extension.compareTo("json") == 0) {
				req.setAttribute("data", data);
				req.getRequestDispatcher("/JsonWriter").forward(req, resp);
			}
		} catch (ServletException e) {
			resp.setStatus(400);
			resp.resetBuffer();
			e.printStackTrace();
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}

}
