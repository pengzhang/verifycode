package com.haotiben.verify.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/get")
public class VerifyCode {
	
	private static Map<String,Response> map = new HashMap<String,Response>();

	@GET
	@Path("/verifycode.json")
	@Produces(MediaType.TEXT_PLAIN)
	public  String getVerifyCode() throws Exception{
		
		int width = 70;
		int height = 40;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		Font font = new Font("Atlantic Inline", Font.BOLD, 30);
		g.setColor(getRandomColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		Random random = new Random();
		String code = "";
		for (int i = 0; i < 4; i++) {
			String str = "";
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			if ("char".equalsIgnoreCase(charOrNum)) {
				str = String.valueOf((char) (97 + random.nextInt(26)));
				code += str;
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				str = String.valueOf(random.nextInt(10));
				code += str;
			}
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(str, 13 * i + 6, 30);
		}
		g.dispose();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", output);
        final byte[] imgData = output.toByteArray();
        final InputStream bigInputStream = new ByteArrayInputStream(imgData);
		map.put(code, Response.ok(bigInputStream).cacheControl(getCacheControl(true)).build());
		
		return code;
	}
	
	@GET
	@Path("/verify/{code}/image.json")
	@Produces("image/jpeg")
	public Response getImage(@PathParam("code")String code){
		
		return map.remove(code);
		
	}
	
	public static CacheControl getCacheControl(boolean falg) {
		CacheControl cc = new CacheControl();
		cc.setMaxAge(60);
		cc.setNoCache(falg);
		return cc;
	}
	
	public static Color getRandomColor(int fc, int bc) throws Exception {
		Random random = new Random();
		if (fc > 255)
			fc = 200;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
}
