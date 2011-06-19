/* 
 This file was generated by Dashcode and is covered by the 
 license.txt included in the project.  You may edit this file, 
 however it is recommended to first turn off the Dashcode 
 code generator otherwise the changes will be lost.
 */
var dashcodePartSpecs = {
    "activityIndicator": { "propertyValues": { "animatingBinding": { "keypath": "podcastDataSource.queryInProgress" }, "visibleBinding": { "keypath": "podcastDataSource.queryInProgress" } }, "view": "DC.ActivityIndicator" },
    "content": { "propertyValues": { "visibleBinding": { "keypath": "podcastDataSource.queryInProgress", "nullValuePlaceholder": true, "transformer": "DC.transformer.Not" } }, "view": "DC.View" },
    "detailBox": { "propertyValues": { "visibleBinding": { "keypath": "episodeList.hasSelection" } }, "view": "DC.View" },
    "detailEpisodeDescription": { "propertyValues": { "htmlBinding": { "keypath": "episodeList.selection.description" } }, "text": "Episode Description", "view": "DC.Text" },
    "detailEpisodeDuration": { "propertyValues": { "textBinding": { "keypath": "episodeList.selection.itunes:duration" } }, "text": "00:00:00", "view": "DC.Text" },
    "detailEpisodeTitle": { "propertyValues": { "textBinding": { "keypath": "episodeList.selection.title" } }, "text": "Episode Title", "view": "DC.Text" },
    "episodeDate": { "propertyValues": { "textBinding": { "keypath": "*.pubDate", "transformer": "articleDateTransformer('', true)" } }, "text": "Publication Date", "view": "DC.Text" },
    "episodeList": { "allowsEmptySelection": true, "dataArray": ["Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7"], "labelElementId": "episodeTitle", "listStyle": "List.EDGE_TO_EDGE", "propertyValues": { "dataArrayBinding": { "keypath": "podcastDataSource.content.channel.item" } }, "sampleRows": 7, "selectionEnabled": true, "useDataSource": true, "view": "DC.List" },
    "episodeTitle": { "propertyValues": { "textBinding": { "keypath": "*.title" } }, "text": "Episode Title", "view": "DC.Text" },
    "image": { "propertyValues": { "srcBinding": { "keypath": "podcastDataSource.content.channel.itunes:image.$href", "multipleValuesPlaceholder": "Images/podcast.png", "noSelectionPlaceholder": "Images/podcast.png", "nullValuePlaceholder": "Images/podcast.png", "transformer": "DC.transformer.FirstObject" } }, "view": "DC.ImageLayout" },
    "media": { "view": "DC.VideoLayout" },
    "playLabel": { "text": "Play Podcast", "view": "DC.Text" },
    "podcastTitle": { "propertyValues": { "textBinding": { "keypath": "podcastDataSource.content.channel.title" } }, "text": "Podcast Title", "view": "DC.Text" },
    "splitLayout1": { "flexibleViewIndex": 1, "initialSize": 833, "initialSplitterSize": 1, "isVertical": true, "splitterPosition": 218, "view": "DC.SplitLayout" },
    "splitLayout2": { "initialSize": 347, "initialSplitterSize": 8, "propertyValues": { "splitterPositionBinding": { "keypath": "episodeList.hasSelection", "transformer": "listSelectionToSplitterPositionTransformer" } }, "splitterPosition": 229, "view": "DC.SplitLayout" },
    "subtitle": { "propertyValues": { "textBinding": { "keypath": "podcastDataSource.content.channel.itunes:subtitle" } }, "text": "Podcast Subtitle", "view": "DC.Text" },
    "summary": { "propertyValues": { "textBinding": { "keypath": "podcastDataSource.content.channel.itunes:summary", "transformer": "DC.transformer.FirstObject" } }, "text": "Podcast Summary", "view": "DC.Text" },
    "title": { "propertyValues": { "textBinding": { "keypath": "podcastDataSource.content.channel.title" } }, "text": "Podcast Name", "view": "DC.Text" }
};




