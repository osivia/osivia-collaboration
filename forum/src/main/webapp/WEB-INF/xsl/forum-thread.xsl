<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:parserContext="org.osivia.services.forum.thread.portlet.model.ForumThreadParserContext">

    <xsl:param name="parserContext"/>
    <xsl:param name="action"/>

    <xsl:output method="html" encoding="UTF-8"/>


    <xsl:template match="HTML">
        <xsl:apply-templates select="BODY"/>
    </xsl:template>

    <xsl:template match="BODY">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="DIV" name="div">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="DIV/text()">
        <xsl:if test="normalize-space(.)">
            <xsl:element name="p">
                <xsl:value-of select="."/>
            </xsl:element>
        </xsl:if>

        <xsl:call-template name="div"/>
    </xsl:template>


    <!-- Add blockquote header -->
    <xsl:template match="BLOCKQUOTE">
        <xsl:choose>
            <xsl:when test="ancestor::BLOCKQUOTE">
                <xsl:element name="p">
                    <xsl:text>[&#8230;]</xsl:text>
                </xsl:element>
            </xsl:when>

            <xsl:otherwise>
                <xsl:element name="blockquote">
                    <xsl:variable name="id" select="@data-id"/>
                    <xsl:variable name="author" select="@data-author"/>

                    <xsl:attribute name="data-id">
                        <xsl:value-of select="$id"/>
                    </xsl:attribute>

                    <xsl:attribute name="data-author">
                        <xsl:value-of select="$author"/>
                    </xsl:attribute>

                    <xsl:if test="$action = 'load'">
                        <xsl:value-of select="parserContext:getBlockquoteHeader($parserContext, $id, $author)" disable-output-escaping="yes"/>
                    </xsl:if>

                    <xsl:apply-templates select="node()|@*"/>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Remove blockquote header -->
    <xsl:template match="BLOCKQUOTE/P[@class = 'blockquote-header']">
        <xsl:choose>
            <xsl:when test="$action = 'save'"/>

            <xsl:otherwise>
                <xsl:call-template name="copy"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="node()|@*" name="copy">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
