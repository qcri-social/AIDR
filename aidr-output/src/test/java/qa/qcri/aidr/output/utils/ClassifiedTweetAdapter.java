/**
 * Last modified: 11/01/2014
 * 
 * The TaggerJsonOutputAdpater class implements Tagger specific
 * json object format. Specifically, it can return null if the 
 * nominal_labels field in a json message is null. 
 *  
 */

package qa.qcri.aidr.output.utils;

import qa.qcri.aidr.common.filter.NominalLabel;

public class ClassifiedTweetAdapter {
		private String created_at;
		private String text;
		private String crisis_code;
		private String crisis_name;

		private String id;
		private String screen_name;

		private NominalLabel[] nominal_labels;

		public String getCreated_at() {
			return created_at;
		}

		public String getText() {
			return text;
		}

		public String getCrisis_code() {
			return crisis_code;
		}

		public String getCrisis_name() {
			return crisis_name;
		}

		public String getId() {
			return id;
		}

		public String getScreen_name() {
			return screen_name;
		}

		public NominalLabel[] getNominal_labels() {
			return nominal_labels;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public void setText(String text) {
			this.text = text;
		}

		public void setCrisis_code(String crisis_code) {
			this.crisis_code = crisis_code;
		}

		public void setCrisis_name(String crisis_name) {
			this.crisis_name = crisis_name;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setScreen_name(String screen_name) {
			this.screen_name = screen_name;
		}

		public void setNominal_labels(NominalLabel[] nominal_labels) {
			this.nominal_labels = nominal_labels;
		}

}