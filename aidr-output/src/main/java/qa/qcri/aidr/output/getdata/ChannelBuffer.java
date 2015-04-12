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


import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;


public class ChannelBuffer {
	public static int MAX_BUFFER_SIZE = 2000;		// number of elements the buffer will hold at any time
	public static int MAX_FETCH_SIZE = 2000;		// max. number of elements to fetch in one op
	private String channelName = null;
	private long lastAddTime;
	private Buffer messageBuffer = null;
	int size = 0;

	private Boolean publiclyListed = true;

	private static Logger logger = Logger.getLogger(ChannelBuffer.class);
	private static ErrorLog elog = new ErrorLog();

	public ChannelBuffer(final String name, final int bufferSize) {
		this.channelName = name;
		this.messageBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(bufferSize));
		this.size = bufferSize;
	}

	public ChannelBuffer(final String name) {
		this.channelName = name;
	}

	public void createChannelBuffer() {
		this.messageBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(MAX_BUFFER_SIZE));
		this.size = MAX_BUFFER_SIZE;
	}

	public void createChannelBuffer(final int bufferSize) {
		this.messageBuffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(bufferSize));
		this.size = bufferSize;
	}

	public void setPubliclyListed(Boolean publiclyListed) {
		this.publiclyListed = publiclyListed;
	}

	public Boolean getPubliclyListed() {
		return publiclyListed;
	}

	@SuppressWarnings("unchecked")
	public void addMessage(String msg) {
		try {
			//synchronized(this.messageBuffer) 
			{
				messageBuffer.add(msg);
				lastAddTime = new Date().getTime();
			}
		} catch (Exception e) {
			logger.error("Couldn't add message to buffer for channel: " + this.channelName);
			logger.error(elog.toStringException(e));
		}
	}

	@SuppressWarnings("unchecked")
	public void addAllMessages(ArrayList<String> msgList) {
		try {
			//synchronized(this.messageBuffer) 
			{
				messageBuffer.addAll(msgList);
				lastAddTime = new Date().getTime();
			}
		} catch (Exception e) {
			logger.error("Couldn't add message to buffer for channel: " + this.channelName);
			logger.error(elog.toStringException(e));
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * @param msgCount: number of messages to return
	 * @return Returns a list of messages sorted in ascending order of timestamp
	 */
	public List<String> getMessages(int msgCount) { 
		//long startTime = System.currentTimeMillis();

		List<String> tempList = null;
		try {
			synchronized(this.messageBuffer) {
				tempList = new ArrayList<String>(this.messageBuffer.size());
				//logger.info("current message buffer size = " + messageBuffer.size());
				// first copy out the entire buffer to a list
				Iterator<String> itr = this.messageBuffer.iterator();
				while (itr.hasNext()) {
					String element = itr.next();
					if (element != null) tempList.add(element);		// only non-null elements
				}
			}
			logger.info("Copied data : " + tempList.size() + ", msgCount:messageBuffer.size = " + msgCount + ":" + messageBuffer.size());
			if (msgCount >= tempList.size()) {
				return tempList;		// optimization
			}

			// Otherwise, get the last msgCount elements, in oldest-first order
			//Collections.reverse(tempList);	// in-situ reversal O(n) time
			List<String> returnList = new ArrayList<String>(msgCount);
			int index = Math.max(0, (tempList.size() - msgCount));	// from where to pick
			for (int i = 0;i < msgCount;i++) {
				returnList.add(tempList.get(index));
				++index;
			}
			logger.info("Fetched size = " + index + " from start loc = " + Math.max(0, (tempList.size() - msgCount)));
			return returnList;
		} catch (Exception e) {
			logger.error("Error in creating list out of buffered messages");
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	public int getCurrentMsgCount() {
		return messageBuffer.size();
	}

	public void deleteBuffer() {
		channelName = null;
		if (messageBuffer != null) {
			messageBuffer.clear();
			messageBuffer = null;
		}
	}

	public void setChannelName(String name) {
		channelName = name;
	}

	public String getChannelName() {
		return channelName;
	}

	public long getLastAddTime() {
		return lastAddTime;
	}

	public int getMaxBufferSize() {
		return MAX_BUFFER_SIZE;
	}

	public int getMaxFetchSize() {
		return MAX_FETCH_SIZE;
	}

	public int getBufferSize() {
		return size;
	}
}