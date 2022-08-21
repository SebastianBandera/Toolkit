package toolkit.core.app;

import java.util.Optional;

public class ToolkitInitParameters {

	private Optional<String> rootPath;
	
	public ToolkitInitParameters() {
		this.rootPath = Optional.empty();
	}

	public Optional<String> getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = Optional.of(rootPath);
	}
}
