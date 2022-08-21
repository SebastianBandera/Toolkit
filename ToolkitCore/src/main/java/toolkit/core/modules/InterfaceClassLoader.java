package toolkit.core.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InterfaceClassLoader extends ClassLoader {

	public InterfaceClassLoader(ClassLoader parent) {
		super(parent);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> cls = loadInterface(name);
		if (cls == null) {
			return super.findClass(name);
		}
		return cls;
	}
	
	private Class<?> loadInterface(String name) {
		if (name!=null && name.startsWith("toolkit.core.api.module.")) {
			byte[] b = null;
			try {
				String parsed = name.replace(".", "/") + ".class";
				b = Files.readAllBytes(new File("F:\\Projectos Java\\Eclipse\\Toolkit\\ToolkitAPIInterface\\target\\classes\\" + parsed).toPath());					
			} catch (IOException e) {
				e.printStackTrace();
			}
			return defineClass(null, b, 0, b.length);
		}
		return null;
	}
}
