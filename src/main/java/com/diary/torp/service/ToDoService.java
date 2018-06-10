package com.diary.torp.service;

import com.diary.torp.UnAuthenticationException;
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

    @Resource(name="commentRepository")
    private CommentRepository commentRepository;

    public ToDoBoard createBoard(User user, String title) {
        ToDoBoard newBoard = new ToDoBoard(user, title);
        return toDoBoardRepository.save(newBoard);
    }

    @Transactional
    public ToDoBoard editBoardTitle(User loginUser, long boardId, String newTitle) throws UnAuthenticationException {
        ToDoBoard targetBoard = toDoBoardRepository.findOne(boardId);

        targetBoard.editTitle(loginUser, newTitle);
        return targetBoard;
    }

    @Transactional
    public ToDoDeck editDeckTitle(User loginUser, long deckId, String newTitle) throws UnAuthenticationException {
        ToDoDeck targetDeck = toDoDeckRepository.findOne(deckId);

        targetDeck.editTitle(loginUser, newTitle);
        return targetDeck;
    }

    @Transactional
    public ToDoCard editCardTitle(User loginUser, long cardId, String newTitle) throws UnAuthenticationException {
        ToDoCard targetCard = toDoCardRepository.findOne(cardId);

        targetCard.editTitle(loginUser, newTitle);
        return targetCard;
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

    public ToDoCard getCardInfo(User loginUser, long cardId) throws Exception {
        ToDoCard toDoCard = toDoCardRepository.findOne(cardId);
        if (toDoCard.isDeleted()) {
            throw new Exception();
        }

        if (!toDoCard.isOwner(loginUser)) {
            throw new UnAuthenticationException();
        }

        return toDoCard;
    }

    public ToDoCard createCard(User loginUser, String title) {
        log.debug("in to ToDoService - createCard");

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

    @Transactional
    public ToDoCard editDescription(User loginUser, long cardId, String newDescription) throws UnAuthenticationException {
        ToDoCard editedCard = toDoCardRepository.findOne(cardId);
        editedCard.editDescription(loginUser, newDescription);
        return editedCard;
    }

    public Comment createComment(User loginUser, String comment) {
        Comment newComment = new Comment(loginUser, comment);
        return commentRepository.save(newComment);
    }

    @Transactional
    public Comment addComment(User loginUser, long cardId, Comment newComment) throws UnAuthenticationException {
        ToDoCard card = toDoCardRepository.findOne(cardId);
        newComment.registerIntoCard(card);
        card.addComment(loginUser, newComment);
        return newComment;
    }

    @Transactional
    public ToDoBoard deleteBoard(User loginUser, long boardId) throws UnAuthenticationException {
        ToDoBoard targetBoard = toDoBoardRepository.findOne(boardId);

        targetBoard.delete(loginUser);
        return targetBoard;
//        for (ToDoDeck deck: targetBoard.getToDoDecks()) {
//
//        }
    }
//
//    public ToDoCard deleteCard(User loginUser, long deckId) {
//        ToDoDeck targetDeck = toDoDeckRepository.findOne(deckId);
//
//        for (ToDoCard card : targetDeck.getToDoCards()) {
//
//        }
//    }
//
//    public Comment deleteComment(User loginUser, long cardId) {
//        ToDoCard targetCard = toDoCardRepository.findOne(cardId);
//
//        for (Comment comment : targetCard.getComments()) {
//            comment.deleteComment(loginUser);
//        }
//    }
}
