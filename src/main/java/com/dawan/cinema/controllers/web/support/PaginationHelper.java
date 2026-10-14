package com.dawan.cinema.controllers.web.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Calcule les numéros de page à afficher dans une pagination compacte :
 * première page, dernière page, page courante et ses voisines, avec des "…" entre les trous.
 * Utilisé depuis Thymeleaf : ${@pagination.items(page)}.
 */
@Component("pagination")
public class PaginationHelper {

    /** Nombre de voisines affichées de chaque côté de la page courante. */
    private static final int WINDOW = 1;

    /**
     * Élément de pagination : un numéro de page (index à partir de 0) ou des points de suspension.
     */
    public record PageItem(int number, boolean ellipsis) {

        static PageItem page(int number) {
            return new PageItem(number, false);
        }

        static PageItem gap() {
            return new PageItem(-1, true);
        }
    }

    /**
     * @param page page courante renvoyée par le service
     * @return les éléments à afficher, dans l'ordre
     */
    public List<PageItem> items(Page<?> page) {
        int total = page.getTotalPages();
        int current = page.getNumber();
        List<PageItem> items = new ArrayList<>();
        int previous = -1;

        for (int i = 0; i < total; i++) {
            if (!isVisible(i, current, total)) {
                continue;
            }
            if (previous >= 0 && i - previous == 2) {
                items.add(PageItem.page(previous + 1));   // trou d'une seule page : on l'affiche
            } else if (previous >= 0 && i - previous > 2) {
                items.add(PageItem.gap());
            }
            items.add(PageItem.page(i));
            previous = i;
        }
        return items;
    }

    private static boolean isVisible(int index, int current, int total) {
        return index == 0 || index == total - 1 || Math.abs(index - current) <= WINDOW;
    }
}