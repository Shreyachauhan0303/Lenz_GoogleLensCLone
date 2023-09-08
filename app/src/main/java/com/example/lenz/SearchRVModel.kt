package com.example.lenz

class SearchRVModel (
    internal var title: String,
    internal var link: String?,
    internal var displayedLink: String,
    internal var snippet: String
    ) {
        // Getter for title (no need for a setter if it's immutable)
        fun getTitle(): String {
            return title
        }
        fun setTitle(){
            this.title=title
    }

        // Getter for link
        fun getLink(): String? {
            return link
        }
        fun setLink(){
            this.link=link
    }

        // Getter for displayedLink (no need for a setter if it's immutable)
        fun getDisplayedLink(): String {
            return displayedLink
        }
        fun setDisplayedLink() {
            this.displayedLink=displayedLink
    }


        // Getter for snippet (no need for a setter if it's immutable)
        fun getSnippet(): String {
            return snippet
        }
        fun setSnippet() {
            this.snippet=snippet
    }



    }