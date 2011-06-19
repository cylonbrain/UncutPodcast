/* 
 This file was generated by Dashcode.  
 You may edit this file to customize your widget or web page 
 according to the license.txt file included in the project.
 */

//
// Function: load()
// Called by HTML body element's onload event when the web application is ready to start
//
function load()
{
    dashcode.setupParts();

    // This message checks for common errors with the RSS feed or setup.
    // The handler will hide the split view and display the error message.
    handleCommonErrors(attributes.dataSource,function(errorMessage){
        var episodesList = document.getElementById('episodes')
        
        if (episodesList) {
            episodesList.style.display = 'none';
        }
                       
        showError(errorMessage);
    });
    
    filterItemsWithMedia('episodes');
}


function rowClicked(event)
{
    var episodeList = dashcode.getDataSource('episodes');
    var selectedMedia = episodeList.valueForKeyPath('selection.enclosure.$url');
    
    document.getElementById("episodes").object.clearSelection(true);
    window.location = selectedMedia;
}