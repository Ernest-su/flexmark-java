package com.vladsch.flexmark.ext.wikilink.internal;

import com.vladsch.flexmark.ext.wikilink.WikiLink;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.internal.util.collection.DataHolder;
import com.vladsch.flexmark.internal.util.sequence.BasedSequence;
import com.vladsch.flexmark.node.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WikiLinkNodeRenderer implements NodeRenderer {
    private static String fromChars = " +/<>";
    private static String toChars = "-----";

    private final WikiLinkOptions options;

    public WikiLinkNodeRenderer(DataHolder options) {
        this.options = new WikiLinkOptions(options);
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return new HashSet<>(Collections.singletonList(WikiLink.class));
    }

    @Override
    public void render(NodeRendererContext context, HtmlWriter html, Node node) {
        if (node instanceof WikiLink) {
            renderWikiLink(context, html, (WikiLink) node);
        }
    }

    private String wikiLinkToUrl(BasedSequence wikiLink) {
        StringBuilder sb = new StringBuilder();
        int iMax = wikiLink.length();
        for (int i = 0; i < iMax; i++) {
            char c = wikiLink.charAt(i);
            int pos = fromChars.indexOf(c);
            if (pos < 0) {
                sb.append(c);
            } else {
                sb.append(toChars.charAt(pos));
            }
        }

        sb.append(options.linkFileExtension);
        return sb.toString();
    }

    private void renderWikiLink(NodeRendererContext context, HtmlWriter html, WikiLink node) {
        String url = wikiLinkToUrl(node.getLink());
        html.attr("href", context.encodeUrl(url));
        html.withAttr().tag("a");
        html.text(node.getText().isNotNull() ? node.getText().toString() : node.getLink().toString());
        html.tag("/a");
    }
}