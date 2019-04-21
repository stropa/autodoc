
joint.shapes.basic.Generic.define('autodoc.Container', {
    attrs: {
        '.uml-state-body': {
            'ref':'.uml-state-events', 'refWidth':'120%',
            'width': 200, 'height': 200, 'rx': 0, 'ry': 0,
            'fill': '#ecf0f1', 'stroke': '#bdc3c7', 'stroke-width': 3
        },
        '.uml-state-separator': {
            'stroke': '#bdc3c7', 'stroke-width': 0
        },
        '.uml-state-name': {
            'ref': '.uml-state-body', 'ref-x': .5, 'ref-y': 5, 'text-anchor': 'middle',
            'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
        },
        '.uml-state-header': {
            'ref':'.uml-state-body',
            'refWidth':'100%',
            'width': 20, 'height': 20, fill: 'rgba(48, 208, 198, 0.08)',
            'stroke': 'rgba(48, 208, 198, 0.4)', 'stroke-width': 2
        },
        '.uml-state-events': {
            'ref': '.uml-state-header', 'ref-x': 5, 'ref-y': '110%', 'refWidth':'100%',
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
    events: []

}, {
    markup: [
        '<g class="scalable"> \
            <rect class="uml-state-body"/> \
        </g> \
        <rect class="uml-state-header"/>  \
        <text class="uml-state-name"/> \
        <text class="uml-state-events"/> \
        <circle class="collapse-tool" r="10"/>'
    ].join(''),

    initialize: function() {

        this.on({
            'change:name': this.updateName,
            'change:events': this.updateEvents
        }, this);

        this.updateName();
        this.updateEvents();

        joint.shapes.basic.Generic.prototype.initialize.apply(this, arguments);
    },

    updateName: function() {

        this.attr('.uml-state-name/text', this.get('name'));
    },

    updateEvents: function() {

        this.attr('.uml-state-events/text', this.get('events').join('\n'));
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
        group.set('events', group.get('eventsCollapsed'));
        group.set('eventsCollapsed', []);
        group.size(group.get('sizeExpanded').width, group.get('sizeExpanded').height)
        //group.fitEmbeds({ padding: { top: 140, left: 10, right: 10, bottom: 10 }});
        group.attr('tool/stroke', 'blue');

    } else {

        // COLLAPSE
        embeds = graph.getSubgraph(group.getEmbeddedCells());
        embeds.sort(function(a) { return a.isLink() ? 1 : - 1; });
        graph.removeCells(embeds);
        // get relative position to parent
        embeds.forEach(function(embed) {
            if (embed.isLink()) return;
            var diff = embed.position().difference(group.position());
            embed.position(diff.x, diff.y);
        });
        // serialize subgraph
        group.set('sizeExpanded', group.size())
        group.set('subgraph', embeds.map(function(embed) { return embed.toJSON(); }));

        group.set('eventsCollapsed', group.get('events'));
        group.set('events', [' ']);

        group.resize(100, 100);
        group.set('collapsed', true);
        group.size(100, 40);
        group.attr('tool/stroke', 'red');
    }
}


