
joint.shapes.basic.Generic.define('autodoc.Container', {
    attrs: {
        '.container-body': {
            'ref':'.element-contents', 'refWidth':'120%', 'refHeight':'100%',
            //'width': 200, 'height': 200,
            'rx': 0, 'ry': 0,
            'fill': '#ecf0f1', 'stroke': '#bdc3c7', 'stroke-width': 3
        },
        '.uml-state-separator': {
            'stroke': '#bdc3c7', 'stroke-width': 0
        },
        '.uml-state-name': {
            'ref': '.uml-state-header', 'ref-x': .5, 'ref-y': 5, 'text-anchor': 'middle',
            'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
        },
        '.uml-state-header': {
            //'ref':'.container-body',
            'refWidth':'100%',
            'width': 20, 'height': 20, fill: 'rgba(48, 208, 198, 0.08)',
            'stroke': 'rgba(48, 208, 198, 0.4)', 'stroke-width': 2
        },
        '.element-contents': {
            'ref': '.uml-state-header',
            'ref-x': 5, 'ref-y': 25,
            'refWidth':'100%',
            'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
        },
        '.collapse-tool': {
            r: 6,
            cx: 10,
            cy: 10,
            fill: '#ecf0f1',
            stroke: '#bdc3c7',
            strokeWidth: .5,
            cursor  : 'pointer',
            event: 'element:collapse'
        }
    },

    name: 'Container',
    attributes: []

}, {
    markup: [
        '<g class="scalable"> \
            <rect class="container-body"/> \
        </g> \
        <rect class="uml-state-header"/>  \
        <text class="uml-state-name"/> \
        <text class="element-contents"/> \
        <circle class="collapse-tool" r="10"/>'
    ].join(''),

    initialize: function() {

        this.on({
            'change:name': this.updateName,
            'change:attributes': this.updateattributes
        }, this);

        this.updateName();
        this.updateattributes();

        joint.shapes.basic.Generic.prototype.initialize.apply(this, arguments);
    },

    updateName: function() {

        this.attr('.uml-state-name/text', this.get('name'));
    },

    updateattributes: function() {

        var text = '';
        let attributes = this.get('attributes');
        var i = 1;
        var nLines = 0;
        var maxLineLength = 0;
        for (a in attributes) {
            text = text + a + ":" + attributes[a] + "\n";
            nLines += 1;
            if (text.length > maxLineLength) {
                maxLineLength = text.length
            }
            if (i >= 2) break;
            i += 1;
        }
        if (text.length === 0 && attributes.length !== 0) {
            text = "\n"
        }
        //this.attr('.element-contents/text', attributes);
        this.attr('.element-contents/text', text);
        this.attributes.numLines = nLines;

        // <g model-id="cc154b2d-961a-4eaf-acdc-065cbbe12b43" >

        if (nLines === 0) {
            if (this.attributes.name)
            this.resize(this.attributes.name.length * 12, 20)
        } else {
            this.resize(maxLineLength * 8 , Math.max(20, 25 + nLines * 16))
        }

    },

});


// Collapse / Expand
function toggleCollapse(group) {
    var graph = group.graph;
    if (!graph) return;
    var embeds;
    if (group.get('collapsed')) {
        // EXPAND
        var subgraph = group.get('subgraph');
        // deserialize subgraph
        var tmpGraph = new joint.dia.Graph();
        tmpGraph.addCells(subgraph);
        embeds = tmpGraph.getCells();
        tmpGraph.removeCells(embeds);
        // set relative position to parent
        embeds.forEach(function(embed) {
            if (embed.isLink()) return;
            var diff = embed.position().offset(group.position());
            embed.position(diff.x, diff.y);
        });
        graph.addCells(embeds);
        embeds.forEach(function(embed) {
            if (embed.isElement()) {
                group.embed(embed);
            } else {
                embed.reparent();
            }
        });
        group.set('collapsed', false);
        //debugger;
        group.set('attributes', group.get('attributesCollapsed'));
        group.get('collapsedLinks').forEach(function (link) {
            link.addTo(graph);
        });
        group.set('attributesCollapsed', []);
        group.size(group.get('sizeExpanded').width, group.get('sizeExpanded').height);
        //group.fitEmbeds({ padding: { top: 140, left: 10, right: 10, bottom: 10 }});
        group.attr('tool/stroke', 'blue');

    } else {

        // COLLAPSE
        embeds = graph.getSubgraph(group.getEmbeddedCells());
        embeds.sort(function(a) { return a.isLink() ? 1 : - 1; });


        // Saving links
        let collapsedLinks = [];
        embeds.forEach( function (emb) {
            let links = graph.getConnectedLinks(emb);
            collapsedLinks = collapsedLinks.concat(links)
        });
        group.set("collapsedLinks", collapsedLinks);

        graph.removeCells(embeds);
        // get relative position to parent
        embeds.forEach(function(embed) {
            if (embed.isLink()) {
                console.log("Collapsing link: " + embed);
                return;
            }
            var diff = embed.position().difference(group.position());
            embed.position(diff.x, diff.y);
            // TODO: when node is collapsed reassign all links leading to[/from?] children to node itself, probably changing link style
            /*var cLinks = graph.getConnectedLinks(embed);
            console.log(cLinks)*/
        });
        // serialize subgraph
        group.set('sizeExpanded', group.size());
        group.set('subgraph', embeds.map(function(embed) { return embed.toJSON(); }));

        group.set('attributesCollapsed', group.get('attributes'));
        group.set('attributes', []);




        //group.resize(100, 100);
        group.set('collapsed', true);
        //group.size(100, 20);
        group.attr('tool/stroke', 'red');
    }
}


