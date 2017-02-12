import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RealSpider {
	private final String HomePage = "https://share.dmhy.org";
	private final String NextPage = HomePage + "/topics/list/page/";
	private String Dir;
	
	public RealSpider(String Dir) {
		this.Dir = Dir;
	}

	
	public String ReadPage(String url) {
		URL ThisPage;
		while(true) {
			try {
				ThisPage = new URL(url);
				URLConnection connection = ThisPage.openConnection();
				connection.setConnectTimeout(5 * 1000);
				connection.setReadTimeout(5 * 1000);
				BufferedReader PageReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				StringBuilder html = new StringBuilder();
				String temp;
				while ((temp = PageReader.readLine()) != null) {
					html.append(temp);
				}
				PageReader.close();
				return html.toString();
			} catch (SocketException e) {
				System.err.println(e.getMessage());
				continue;
			} catch (IOException e) { 
				System.err.println(e.getMessage());
				continue;
			}
		}
			
	}
	
	public Resource getResourceByHtml(String html) {
		Pattern p = Pattern.compile("\\s+?\\S{0,3}<font color=.?\\w+?>(\\W{1,5})</font>.+?"
				+ "target=\"_blank\" >\\s+(.+?)</a>.+?(magnet:\\?xt=urn:btih:.+?)\">&nbsp;");
		Matcher m = p.matcher(html);
		Resource re = new Resource();
		while (m.find()) {
			re.Category.add(m.group(1));
			re.DirName.add(m.group(2).replaceAll("[\\.+\t\\\\?\"/<>*|:／]+", " ").trim());
			re.torrent.add(m.group(3));
		}
		return re;
	}
	
	public void Download(Resource re) {
		if(!re.IsEqual()) {
			System.err.println("数量不符");
		} else {
			for (int i = 0; i < re.torrent.size(); i++) {
				File file = new File(Dir + "\\" + re.Category.get(i) + 
						"\\" + re.DirName.get(i) + "\\" + "torrent.txt");
				file.getParentFile().mkdirs();
				if (!file.exists()) {
					try {
						file.createNewFile();
						FileWriter out = new FileWriter(file);
						out.write(re.torrent.get(i));
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(file);
					}
				} 
			}
		}
	}
	
	public String getHomePage() {
		return HomePage;
	}
	
	public String getNextPage(int Page) {
		return NextPage + Integer.toString(Page);
	}
	
	public void StartSpider(int Start, int End) {
		String html;
		for (int i = Start; i <= End; i++) {
			System.out.println("Page:" + i);
			if (i == 1) {
				html = ReadPage(getHomePage());
			} else {
				html = ReadPage(getNextPage(i));
			}
			Resource re = getResourceByHtml(html);
			Download(re);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void StartSpider() {
		String html;
		int i = 0;
		while (true) {
			i++;
			System.out.println("Page:" + i);
			if (i == 1) {
				html = ReadPage(getHomePage());
			} else {
				html = ReadPage(getNextPage(i));
			}
			Resource re = getResourceByHtml(html);
			if (re.torrent.size() == 0) break;
			Download(re);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		RealSpider spider = new RealSpider("F:\\dmhy");
		spider.StartSpider();
	}
}


class Resource {
	public List<String> Category;
	public List<String> DirName;
	public List<String> torrent;
	
	public Resource() {
		DirName = new ArrayList<String>();
		torrent = new ArrayList<String>();
		Category = new ArrayList<String>();
	}
	
	public boolean IsEqual() {
		if (Category.size() == torrent.size() 
				&& torrent.size() == DirName.size()) {
			return true;
		} else return false;
	}
}
