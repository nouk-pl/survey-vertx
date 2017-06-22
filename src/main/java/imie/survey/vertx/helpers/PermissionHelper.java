package imie.survey.vertx.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PermissionHelper {
	
	public enum Direction {
		INBOUND,
		OUTBOUND,
		BOTHSIDE;
	}
	
	private Map<String, Direction> messagesPermissions;
	
	private PermissionHelper() {
		messagesPermissions = new HashMap<>();
	}
	
	public static PermissionHelper create() {
		return new PermissionHelper();
	}
	
	public PermissionHelper inBound(String... addresses) {
		for (String addr : addresses) {
			messagesPermissions.put(addr, Direction.INBOUND);
		}
		return this;
	}
	
	public PermissionHelper inBound(Iterable<String> addresses) {
		for (Iterator<String> it = addresses.iterator(); it.hasNext(); ) {
			String addr = it.next();
			messagesPermissions.put(addr, Direction.INBOUND);
		}
		return this;
	}
	
	public PermissionHelper outBound(String... addresses) {
		for (String addr : addresses) {
			messagesPermissions.put(addr, Direction.OUTBOUND);
		}
		return this;
	}
	
	public PermissionHelper outBound(Iterable<String> addresses) {
		for (Iterator<String> it = addresses.iterator(); it.hasNext(); ) {
			String addr = it.next();
			messagesPermissions.put(addr, Direction.OUTBOUND);
		}
		return this;
	}
	
	public PermissionHelper bothSide(String... addresses) {
		for (String addr : addresses) {
			messagesPermissions.put(addr, Direction.BOTHSIDE);
		}
		return this;
	}
	
	public PermissionHelper bothSide(Iterable<String> addresses) {
		for (Iterator<String> it = addresses.iterator(); it.hasNext(); ) {
			String addr = it.next();
			messagesPermissions.put(addr, Direction.BOTHSIDE);
		}
		return this;
	}
	
	public Map<String, Direction> getPermissionsMap() {
		return this.messagesPermissions;
	}

}
