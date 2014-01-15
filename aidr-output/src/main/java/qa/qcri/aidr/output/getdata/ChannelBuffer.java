/** 
 * @author Koushik Sinha
 * Last modified: 06/01/2014
 * 
 * The ChannelBuffer class implements the creation,
 * and deletion of channels as well as retrieval of
 * messages from a specific channel.
 * 
 */

package qa.qcri.aidr.output.getdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelBuffer {
	private static int MAX_BUFFER_SIZE = 2000;		// number of elements the buffer will hold at any time
	private String channelName;
	private long lastAddTime;
	private Buffer messageBuffer;
	
	private static Logger logger = LoggerFactory.getLogger(ChannelBuffer.class);
	
	public ChannelBuffer(final String name) {
		//BasicConfigurator.configure();			// setup log4j logging
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
				
		this.channelName = name;
	}
	
	public ChannelBuffer createChannelBuffer() {
		this.messageBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(MAX_BUFFER_SIZE));
		logger.info("[createChannelBuffer] Create new channel buffer with default size = " + MAX_BUFFER_SIZE + "::" + this.messageBuffer.size());
		return this;
	}

	public ChannelBuffer createChannelBuffer(final int bufferSize) {
		this.messageBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(bufferSize));
		logger.info("[createChannelBuffer] Create new channel buffer with size = " + bufferSize + "::" + this.messageBuffer.size());
		return this;
	}

	@SuppressWarnings("unchecked")
	public void addMessage(String msg) {
		this.messageBuffer.add(msg);
		this.lastAddTime = new Date().getTime();
		logger.debug("[addMessage] Added message to messageBuffer, buffer size = " + this.messageBuffer.size());
	}

	@SuppressWarnings("unchecked")
	public void addAllMessages(ArrayList<String> msgList) {
		this.messageBuffer.addAll(msgList);
		this.lastAddTime = new Date().getTime();
		logger.debug("[addAllMessages] Added message to messageBuffer, buffer size = " + this.messageBuffer.size());
	}

	@SuppressWarnings("unchecked")
	public List<String> getMessages(int msgCount) {
		List<String> msgList = new ArrayList<String>(); 
		synchronized(this) {
			Iterator<String>itr = this.messageBuffer.iterator();
			int count = 0;
			while (itr.hasNext()) {
				msgList.add(itr.next());
				++count;
			}
			logger.debug("[getMessages] Returned messages count = " + count);
		}
		return msgList;
	}
	
	public void deleteBuffer() {
		this.channelName = null;
		this.messageBuffer.clear();
		this.messageBuffer = null;
		logger.info("[deleteBuffer] Deleted buffer.");
		
	}
	
	public void setChannelName(String name) {
		this.channelName = name;
	}

	public String getChannelName() {
		return this.channelName;
	}
	
	public long getLastAddTime() {
		return this.lastAddTime;
	}
}