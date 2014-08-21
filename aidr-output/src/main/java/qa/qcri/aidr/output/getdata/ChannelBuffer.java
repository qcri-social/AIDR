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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

import org.apache.commons.lang.ArrayUtils;
//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.output.utils.ErrorLog;

public class ChannelBuffer {
	public static int MAX_BUFFER_SIZE = 2000;		// number of elements the buffer will hold at any time
	public static int MAX_FETCH_SIZE = 2000;		// max. number of elements to fetch in one op
	private String channelName = null;
	private long lastAddTime;
	private Buffer messageBuffer = null;
	int size = 0;

	private Boolean publiclyListed = true;

	private static Logger logger = LoggerFactory.getLogger(ChannelBuffer.class);
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
			messageBuffer.add(msg);
			lastAddTime = new Date().getTime(); 
		} catch (Exception e) {
			logger.error("Couldn't add message to buffer for channel: " + this.channelName);
			logger.error(elog.toStringException(e));
		}
	}

	@SuppressWarnings("unchecked")
	public void addAllMessages(ArrayList<String> msgList) {
		try {
			messageBuffer.addAll(msgList);
			lastAddTime = new Date().getTime();
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
			    tempList = new ArrayList<String>(messageBuffer.size());
				// first copy out the entire buffer to a list
				Iterator<String> itr = messageBuffer.iterator();
				while (itr.hasNext()) {
					String element = itr.next();
					if (element != null) tempList.add(element);		// only non-null elements
				}
			}
			if (msgCount == messageBuffer.size()) {
				return tempList;		// optimization
			}

			// Otherwise, get the last msgCount elements, in oldest-first order
			//Collections.reverse(tempList);	// in-situ reversal O(n) time
			List<String> returnList = new ArrayList<String>(msgCount);
			for (int i = 0;i < msgCount;i++) {
				int index = Math.max(0, (tempList.size() - msgCount));	// from where to pick
				returnList.add(tempList.get(index));
				++index;
			}
			return returnList;
		} catch (Exception e) {
			logger.error("Error in creating list out of buffered messages");
			logger.error(elog.toStringException(e));
			return null;
		}

		/*
		Object[] msgArray = null;
		synchronized(this.messageBuffer) {
			try {
				msgArray = messageBuffer.toArray();
			} catch (Exception e) {
				logger.error("Couldn't convert buffer to array!");
				logger.error(elog.toStringException(e));
			}
		}
		String[] temp = new String[msgCount];
		try {
			System.arraycopy(msgArray, Math.max(0, (msgArray.length-msgCount)), temp, 0, Math.min(msgCount, msgArray.length));
		} catch (Exception e) {
			logger.error("msgArray length = " + msgArray.length + ", start = " + Math.max(0, (msgArray.length-msgCount)) + ", count = " + Math.min(msgCount, msgArray.length));
			logger.error("temp array length = " + temp.length);
			logger.error(elog.toStringException(e));
			return null;
		}
		// Finally remove "null" elements and return the array
		try {
			List<String> fetchedList = new ArrayList<String>(Arrays.asList(temp));
			fetchedList.removeAll(Collections.singleton(null));		// remove null values from list

			//logger.info("Actual time taken to retrieve from channel " + channelName + " = " + (System.currentTimeMillis() - startTime));
			if (fetchedList.isEmpty()) return null;
			return fetchedList;
		} catch (Exception e) {
			logger.error("Error in creating list out of fetched array");
			logger.error(elog.toStringException(e));
			return null;
		}
		 */
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