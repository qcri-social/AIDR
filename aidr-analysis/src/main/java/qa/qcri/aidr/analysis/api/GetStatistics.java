package qa.qcri.aidr.analysis.api;

import qa.qcri.aidr.output.getdata.ChannelBufferManager;


public abstract class GetStatistics {

	// Related to channel buffer management
	public static final String CHANNEL_REG_EX = "aidr_predict.*";
	public static final String CHANNEL_PREFIX_STRING = "aidr_predict.";

	public volatile static ChannelBufferManager masterCBManager = null; 			// managing buffers for each publishing channel

}
