package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class CellRenderer extends JLabel implements ListCellRenderer<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		setText(value.toString());

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {

			background = new Color(236, 240, 241);
			foreground = new Color(44, 62, 80);

			// check if this cell is selected
		} else if (isSelected) {
			background = new Color(236, 240, 241);
			foreground = new Color(44, 62, 80);

			// unselected, and not the DnD drop location
		} else {
			background = new Color(44, 62, 80);
			foreground = new Color(236, 240, 241);
		}
		;

		setBackground(background);
		setForeground(foreground);

		return this;
	}
}
