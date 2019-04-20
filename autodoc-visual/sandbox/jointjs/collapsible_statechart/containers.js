
joint.shapes.basic.Generic.define('autodoc.Container', {
    attrs: {
        '.uml-state-body': {
            'width': 200, 'height': 200, 'rx': 0, 'ry': 0,
            'fill': '#ecf0f1', 'stroke': '#bdc3c7', 'stroke-width': 3
        },
        '.uml-state-separator': {
            'stroke': '#bdc3c7', 'stroke-width': 2
        },
        '.uml-state-name': {
            'ref': '.uml-state-body', 'ref-x': .5, 'ref-y': 5, 'text-anchor': 'middle',
            'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
        },
        '.uml-state-events': {
            'ref': '.uml-state-separator', 'ref-x': 5, 'ref-y': 5,
            'fill': '#000000', 'font-family': 'Courier New', 'font-size': 14
        },
        'tool': {
            r: 10,
            cx: 20,
            cy: 20,
            fill: 'white',
            stroke: 'blue',
            strokeWidth: 2,
            cursor  : 'pointer',
            event: 'element:collapse'
        }
    },

    name: 'Container',
    events: []

}, {
    markup: [
        '<g class="rotatable">',
        '<g class="scalable">',
        '<rect class="uml-state-body"/>',
        '</g>',
        '<path class="uml-state-separator"/>',
        '<text class="uml-state-name"/>',
        '<text class="uml-state-events"/>',
        '</g>',
        '<circle joint-selector="tool" r="30"/>'
    ].join(''),

    initialize: function() {

        this.on({
            'change:name': this.updateName,
            'change:events': this.updateEvents,
            'change:size': this.updatePath
        }, this);

        this.updateName();
        this.updateEvents();
        this.updatePath();

        joint.shapes.basic.Generic.prototype.initialize.apply(this, arguments);
    },

    updateName: function() {

        this.attr('.uml-state-name/text', this.get('name'));
    },

    updateEvents: function() {

        this.attr('.uml-state-events/text', this.get('events').join('\n'));
    },

    updatePath: function() {

        var d = 'M 0 20 L ' + this.get('size').width + ' 20';

        // We are using `silent: true` here because updatePath() is meant to be called
        // on resize and there's no need to to update the element twice (`change:size`
        // triggers also an update).
        this.attr('.uml-state-separator/d', d, { silent: true });
    }
});
