package com.diary.torp.service;

import com.diary.torp.domain.*;
import com.diary.torp.web.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ToDoService {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource(name="toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Resource(name="toDoDeckRepository")
    private ToDoDeckRepository toDoDeckRepository;

    @Resource(name="toDoCardRepository")
    private ToDoCardRepository toDoCardRepository;

    public ToDoBoard createBoard(User user, String title) {
        ToDoBoard newBoard = new ToDoBoard(user, title);
        return toDoBoardRepository.save(newBoard);
    }

    public ToDoDeck createDeck(User user, String title) {
        log.debug("in to ToDoService - createDeck");
        ToDoDeck newDeck = new ToDoDeck(user, title);
        log.debug("new deck title " + newDeck.getTitle() + " , new deck writer is " + newDeck.getWriter());
        return toDoDeckRepository.save(newDeck);
    }

    @Transactional
    public void addDeck(long boardId, ToDoDeck newToDoDeck) {
        log.debug("in to ToDoService - addDeck");
        ToDoBoard board = toDoBoardRepository.findOne(boardId);
        newToDoDeck.registerIntoBoard(board);
        board.addDeck(newToDoDeck);
        return;
    }

    public ToDoCard createCard(User loginUser, String title, long boardId, long deckId) {
        log.debug("in to ToDoService - createCard");
        ToDoBoard board = toDoBoardRepository.findOne(boardId);
        ToDoDeck deck = toDoDeckRepository.findOne(deckId);

        ToDoCard newCard = new ToDoCard(loginUser, title);
        return toDoCardRepository.save(newCard);
    }

    @Transactional
    public void addCard(long deckId, ToDoCard newCard) {
        log.debug("in to ToDoService - addCard");
        ToDoDeck deck = toDoDeckRepository.findOne(deckId);
        newCard.registerIntoDeck(deck);
        deck.addCard(newCard);
    }
}
