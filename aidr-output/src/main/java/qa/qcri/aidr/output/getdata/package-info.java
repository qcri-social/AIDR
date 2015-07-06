/**
 * Methods to get classified documents as JSON objects.
 * 
 *  This package provides the functionality of automatically buffering classified documents
 *  from REDIS (aidr_predict.* channels) using a circular buffer. 
 *  
 *   The package provides REST API interfaces and implementations for:
 *   	1. obtaining the latest 'k' classified documents for a specified active collection 
 *   	2. obtaining the latest 'k' classified documents across all currently active collections
 *   	3. obtaining the list of active collections for which the aidr-tagger module is currently classifying
 *   	4. PING test 
 */
package qa.qcri.aidr.output.getdata;